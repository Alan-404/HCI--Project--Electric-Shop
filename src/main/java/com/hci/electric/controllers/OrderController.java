package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.order.CreateOrderRequest;
import com.hci.electric.dtos.order.CreateOrderResponse;
import com.hci.electric.dtos.order.PayWithCreditResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.models.Cart;
import com.hci.electric.models.CartItem;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Order;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.CartItemService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Color;
import com.hci.electric.utils.Enums;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.Stripe;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;
    private final BillService billService;
    private final ProductDetailService productDetailService;
    private final WarehouseService warehouseService;
    private final WarehouseHistoryService history;
    private final DiscountService discountService;
    private final AccountService accountService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final Auth auth;
    
    @Value("${payment.success}")
    private String successUrl;

    @Value("${payment.cancel}")
    private String cancelUrl;

    @Value("${stripe.secret_key}")
    private String stripeSecretKey;

    public OrderController(
        OrderService orderService,
        CartService cartService,
        BillService billService,
        AccountService accountService,
        DiscountService discountService,
        WarehouseService warehouseService,
        ProductDetailService productDetailService,
        WarehouseHistoryService history,
        CartItemService cartItemService,
        ProductService productService,
        ProductImageService productImageService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.billService = billService;
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.warehouseService = warehouseService;
        this.discountService = discountService;
        this.history = history;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.auth = new Auth(this.accountService);
        this.productImageService = productImageService;
    }

    

    public void warehouseHandle(Warehouse warehouse, int quantityGotten){
        warehouse.setQuantity(warehouse.getQuantity() - quantityGotten);
        this.warehouseService.edit(warehouse);

        WarehouseHistory record = new WarehouseHistory();
        record.setWarehouseId(warehouse.getId());
        record.setQuantity(quantityGotten);
        record.setType(Enums.TypeWarehouse.MINUS.toString());
        this.history.save(record);
    }

    @PostMapping("/add")
    public ResponseEntity<CreateOrderResponse> createOrder(
        HttpServletRequest httpServletRequest,
        @RequestBody CreateOrderRequest request) {

        CreateOrderResponse response = new CreateOrderResponse();
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            response.setMessage("You are not loggged in.");

            return ResponseEntity.status(401).body(response);
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart == null) {
            response.setMessage("Cannot find your cart.");

            return ResponseEntity.status(400).body(response);
        }

        List<CartItem> items = this.cartItemService.getByCartAndStatus(cart.getId(), true);

        if (items == null || items.size() == 0) {
            response.setMessage("Your cart is empty.");

            return ResponseEntity.status(404).body(response);
        }

        List<Double> productsPrice = new ArrayList<>();
        List<Double> productPrice = new ArrayList<>();

        List<Warehouse> warehouses = new ArrayList<>();

        
        List<Bill> pendingPayBills = this.billService.getBillsByPaymentTypeAndStatus("cash", "Processing");
        List<Order> pendingPayOrders = new ArrayList<>();

        for (Bill pendingBill : pendingPayBills) {
            List<Order> billPendingOrders = this.orderService.getByBillId(pendingBill.getId());
            pendingPayOrders.addAll(billPendingOrders);
        }

        for (CartItem item : items) {
            ProductDetail product = this.productDetailService.getById(item.getProductId());
            Warehouse warehouse = this.warehouseService.getByProductId(item.getProductId());

            int totalQuantity = item.getQuantity();

            for (Order pendingOrder : pendingPayOrders) {
                if (pendingOrder.getId() == item.getId()) {
                    totalQuantity += pendingOrder.getQuantity();
                }
            }

            if (warehouse.getQuantity() < totalQuantity) {
                response.setMessage("Product is out of stock.");

                return ResponseEntity.status(400).body(response);
            }

            warehouses.add(warehouse);
            Discount discount = this.discountService.getByProductId(item.getProductId());

            if (discount == null || discount.isStatus() == false) {
                productsPrice.add(item.getQuantity()*product.getPrice());
                productPrice.add(product.getPrice());
            }
            else {
                productsPrice.add(item.getQuantity()*product.getPrice()*((100-discount.getValue())/100));
                productPrice.add(product.getPrice()*((100-discount.getValue())/100));
            }
        }

        Bill bill = new Bill();
        bill.setUserId(account.getUserId());
        bill.setPaymentType(request.getPaymentType());
        bill.setStatus(request.getStatus());
        bill.setPrice(productsPrice.stream().mapToDouble(Double::doubleValue).sum());
        bill.setDeliveryInfoId(request.getShippingAddress());

        Bill savedBill = this.billService.save(bill);

        if (savedBill == null) {
            response.setMessage("Something went wrong.");

            return ResponseEntity.status(500).body(response);
        }

        for (int i=0; i<items.size(); i++){
            Order order = new Order();
            order.setBillId(savedBill.getId());
            order.setProductId(items.get(i).getProductId());
            order.setQuantity(items.get(i).getQuantity());
            order.setProductPrice(productPrice.get(i));
            ProductDetail productDetail = this.productDetailService.getById(order.getProductId());

            productDetail.setTotalSales(productDetail.getTotalSales() + order.getQuantity());

            this.productDetailService.edit(productDetail);

            this.orderService.saveRecord(order, false);
            this.warehouseHandle(warehouses.get(i), items.get(i).getQuantity());
        }

        this.cartItemService.deleteAll(items);

        return ResponseEntity.status(200).body(new CreateOrderResponse(true, "Create Order Successfully", bill));
    }

    @PostMapping("/credit")
    public ResponseEntity<PayWithCreditResponse> payWithCredit(
        HttpServletRequest httpServletRequest,
        @RequestBody CreateOrderRequest request) {
        
        PayWithCreditResponse response = new PayWithCreditResponse();
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            response.setMessage("You are not logged in.");
            return ResponseEntity.status(401).body(response);
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart == null) {
            response.setMessage("Cannot find your cart.");
            return ResponseEntity.status(404).body(response);
        }

        List<CartItem> items = this.cartItemService.getByCartAndStatus(cart.getId(), true);

        if (items == null || items.size() == 0){
            response.setMessage("Your cart is empty.");
            return ResponseEntity.status(404).body(response);
        }

        Stripe.apiKey = stripeSecretKey;
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        List<Double> productsPrice = new ArrayList<>();
        List<Double> productPrice = new ArrayList<>();

        for (CartItem item : items) {
            Warehouse warehouse = this.warehouseService.getByProductId(item.getProductId());

            if (warehouse.getQuantity() < item.getQuantity()) {
                response.setMessage("Product is out of stock.");
                return ResponseEntity.status(400).body(response);
            }

            ProductDetail productDetail = this.productDetailService.getById(item.getProductId());
            Product productOrigin = this.productService.getById(productDetail.getProductId());
            Discount discount = this.discountService.getByProductId(productDetail.getId());
            List<ProductImage> images = this.productImageService.getMediaByProduct(productDetail.getId());
            

            double realPrice = (productDetail.getPrice() - productDetail.getPrice() * (discount.getValue() / 100)) * 100;
            String productName = productOrigin.getName() + " " +
                productDetail.getSpecifications() + " " +
                Color.COLORS[productDetail.getColor()];

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(new Long(item.getQuantity()))
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(new Long((int)realPrice))
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(productName)
                                .addImage(images.get(0).getLink())
                                .build())
                        .build())
                .build();

            lineItems.add(lineItem);
            productsPrice.add(item.getQuantity() * productDetail.getPrice() * ((100-discount.getValue()) / 100));
            productPrice.add(productDetail.getPrice() * ((100 - discount.getValue()) / 100));
        }

        Bill bill = new Bill();
        bill.setUserId(account.getUserId());
        bill.setPaymentType(request.getPaymentType());
        bill.setStatus(request.getStatus());
        bill.setPrice(productsPrice.stream().mapToDouble(Double::doubleValue).sum());
        bill.setDeliveryInfoId(request.getShippingAddress());

        Bill savedBill = this.billService.save(bill);
        if (savedBill == null) {
            response.setMessage("Something went wrong.");

            return ResponseEntity.status(500).body(response);
        }

        for (int i = 0; i < items.size(); i++){
            Order order = new Order();

            order.setBillId(savedBill.getId());
            order.setProductId(items.get(i).getProductId());
            order.setQuantity(items.get(i).getQuantity());
            order.setProductPrice(productPrice.get(i));

            this.orderService.saveRecord(order, false);
        }

        this.cartItemService.deleteAll(items);

        SessionCreateParams params =  SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .addAllLineItem(lineItems)
            .putMetadata("userId", account.getUserId())
            .putMetadata("billId", bill.getId())
            .build();

        try {
            Session session = Session.create(params);
            response.setUrl(session.getUrl());
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setMessage("Something went wrong.");

            return ResponseEntity.status(500).body(response);
        }

        
        response.setSuccess(true);
        return ResponseEntity.status(201).body(response);
    }
}
