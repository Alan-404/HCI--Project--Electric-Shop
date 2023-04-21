package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.order.CreateOrderRequest;
import com.hci.electric.dtos.order.CreateOrderResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.models.Cart;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Order;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Enums;


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
    private final Auth auth;

    public OrderController(OrderService orderService, CartService cartService, BillService billService, AccountService accountService, DiscountService discountService, WarehouseService warehouseService, ProductDetailService productDetailService, WarehouseHistoryService history){
        this.cartService = cartService;
        this.orderService = orderService;
        this.billService = billService;
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.warehouseService = warehouseService;
        this.discountService = discountService;
        this.history = history;

        this.auth = new Auth(this.accountService);
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

    /* @PostMapping("/add")
    public ResponseEntity<CreateOrderResponse> createOrder(HttpServletRequest httpServletRequest, @RequestBody CreateOrderRequest request){
        String token = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(404).body(new CreateOrderResponse(false, "Invalid Token", null));
        }

        List<Cart> items = this.cartService.getByUserIdAndStatus(account.getUserId(), true);
        if (items == null || items.size() == 0){
            return ResponseEntity.status(404).body(new CreateOrderResponse(false, "Not found any things in your cart", null));
        }

        List<Double> productsPrice = new ArrayList<>();
        List<Double> productPrice = new ArrayList<>();

        List<Warehouse> warehouses = new ArrayList<>();

        for (Cart cart : items) {
            ProductDetail product = this.productDetailService.getById(cart.getProductId());
            Warehouse warehouse = this.warehouseService.getByProductId(cart.getProductId());
            if (warehouse.getQuantity() < cart.getQuantity()){
                return ResponseEntity.status(400).body(new CreateOrderResponse(false, "Your Order is Out of Stock", null));
            }
            warehouses.add(warehouse);
            Discount discount = this.discountService.getByProductId(cart.getProductId());
            if (discount == null || discount.isStatus() == false){
                productsPrice.add(cart.getQuantity()*product.getPrice());
                productPrice.add(product.getPrice());
            }
            else{
                productsPrice.add(cart.getQuantity()*product.getPrice()*((100-discount.getValue())/100));
                productPrice.add(product.getPrice()*((100-discount.getValue())/100));
            }
        }

        Bill bill = new Bill();
        bill.setUserId(account.getUserId());
        bill.setPaymentType(request.getPaymentType());
        bill.setStatus(request.getStatus());
        bill.setPrice(productsPrice.stream().mapToDouble(Double::doubleValue).sum());

        Bill savedBill = this.billService.save(bill);
        if (savedBill == null){
            return ResponseEntity.status(500).body(new CreateOrderResponse(false, "Internal Error Server", null));
        }

        for (int i=0; i<items.size(); i++){
            Order order = new Order();
            order.setBillId(savedBill.getId());
            order.setProductId(items.get(i).getProductId());
            order.setQuantity(items.get(i).getQuantity());
            order.setProductPrice(productPrice.get(i));

            this.orderService.saveRecord(order, false);

            this.warehouseHandle(warehouses.get(i), items.get(i).getQuantity());
        }

        this.cartService.deleteCarts(items);

        return ResponseEntity.status(200).body(new CreateOrderResponse(true, "Create Order Successfully", bill));
    }


    @GetMapping("/review")
    public ResponseEntity<List<Order>> getOrderItemByReviewedStatus(HttpServletRequest httpServletRequest,@RequestParam("reviewed") boolean reviewed,  @RequestParam("status") String status){
        String token = httpServletRequest.getHeader("Authorization");
        status = status.toUpperCase();
        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(new ArrayList<>());
        }

        List<Bill> statusBill = this.billService.getByUserIdAndStatus(account.getUserId(), status);
        List<Order> orders = new ArrayList<>();
        for (Bill bill : statusBill) {
            List<Order> ordersStatus = this.orderService.getByBillAndReviewedStatus(bill.getId(), reviewed);
            orders.addAll(ordersStatus);
        }   

        return ResponseEntity.status(200).body(orders);
    } */
}
