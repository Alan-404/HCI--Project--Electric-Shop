package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.bill.BillDetail;
import com.hci.electric.dtos.bill.BillInfo;
import com.hci.electric.dtos.bill.BillResponse;
import com.hci.electric.dtos.bill.EditBillRequest;
import com.hci.electric.dtos.bill.EditBillResponse;
import com.hci.electric.dtos.bill.GetMyBillsResponse;
import com.hci.electric.dtos.bill.OrderItem;
import com.hci.electric.dtos.bill.PaginateBill;
import com.hci.electric.dtos.bill.ShippingAddressResponse;
import com.hci.electric.dtos.productDetail.ProductItem;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.models.DeliveryInfo;
import com.hci.electric.models.Order;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.User;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.DeliveryInfoService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.UserService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Color;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;
    private final OrderService orderService;
    private final AccountService accountService;
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final Auth auth;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductImageService productImageService;
    private final WarehouseService warehouseService;
    private final WarehouseHistoryService warehouseHistoryService;
    private final DeliveryInfoService deliveryInfoService;

    public BillController(
        BillService billService,
        OrderService orderService,
        AccountService accountService,
        ProductService productService,
        ProductDetailService productDetailService,
        UserService userService,
        ProductImageService productImageService,
        WarehouseService warehouseService,
        WarehouseHistoryService warehouseHistoryService,
        DeliveryInfoService deliveryInfoService) {
        this.billService = billService;
        this.orderService = orderService;
        this.accountService = accountService;
        this.productDetailService = productDetailService;
        this.productService = productService;
        this.modelMapper = new ModelMapper();
        this.auth = new Auth(this.accountService);
        this.userService = userService;
        this.productImageService = productImageService;
        this.warehouseService = warehouseService;
        this.warehouseHistoryService = warehouseHistoryService;
        this.deliveryInfoService = deliveryInfoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBill(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        Bill bill = this.billService.getById(id);
        if (bill.getUserId().equals(account.getUserId()) == false){
            return ResponseEntity.status(403).body(null);
        } 

        BillResponse response = this.modelMapper.map(bill, BillResponse.class);
        List<Order> orders = this.orderService.getByBillId(bill.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (Order order : orders) {
            OrderItem orderItem = this.modelMapper.map(order, OrderItem.class);
            ProductDetail detail = this.productDetailService.getById(order.getProductId());
            Product origin = this.productService.getById(detail.getProductId());
            List<ProductImage> images = this.productImageService.getMediaByProduct(detail.getId());

            orderItem.setProductName(
                origin.getName() + " " +
                detail.getSpecifications() + " " +
                Color.COLORS[detail.getColor()]);
            
            orderItem.setImage(images.get(0).getLink());
            orderItems.add(orderItem);
        }

        if (bill.getDeliveryInfoId() != 0) {
            Optional<DeliveryInfo> deliveryInfo = this.deliveryInfoService.getById(bill.getDeliveryInfoId());
            
            if (deliveryInfo != null) {
                ShippingAddressResponse shippingAddress = this.modelMapper.map(deliveryInfo, ShippingAddressResponse.class);
                response.setShippingAddress(shippingAddress);
            }
        } else {
            User currentUser = this.userService.getById(account.getUserId());

            ShippingAddressResponse shippingAddress = new ShippingAddressResponse();
            shippingAddress.setAcceptorName(currentUser.getFirstName() + " " + currentUser.getLastName());
            shippingAddress.setAcceptorPhone(currentUser.getPhone());
            shippingAddress.setDeliveryAddress(currentUser.getAddress());

            response.setShippingAddress(shippingAddress);
        }

        response.setOrderItems(orderItems);
        response.setOrderDate(bill.getOrderTime());

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/api")
    public ResponseEntity<List<Bill>> getBills(
        @RequestParam(name = "num", required = false) Integer num,
        @RequestParam(name = "page", required = false) Integer page,
        HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            return ResponseEntity.status(400).body(null);
        }

        if (page == null) {
            page = 1;
        }

        int totalBills = this.billService.getByUserId(account.getUserId()).size();

        if (num == null) {
            num = totalBills;
        }

        List<Bill> bills = this.billService.paginateBillsByUserId(account.getUserId(), page, num);

        return ResponseEntity.status(200).body(bills);
    }

    @GetMapping("/show")
    public ResponseEntity<PaginateBill> getBills(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer num){
        

        int totalItems = this.billService.getAll().size();
        if (page == null){
            page = 1;
        }

        if (num == null){
            num = totalItems;
        }

        List<Bill> bills = this.billService.paginateBills(page, num);

        if (bills == null){
            return ResponseEntity.status(500).body(new PaginateBill(new ArrayList<>(), 0));
        }

        List<BillResponse> items = new ArrayList<>();

        for (Bill bill : bills) {
            BillResponse response = this.modelMapper.map(bill, BillResponse.class);
            List<Order> orders = this.orderService.getByBillId(bill.getId());
            
            List<OrderItem> orderItems = new ArrayList<>();

            for (Order order : orders) {
                OrderItem orderItem = this.modelMapper.map(order, OrderItem.class);
                ProductDetail detail = this.productDetailService.getById(order.getProductId());
                Product origin = this.productService.getById(detail.getProductId());
                List<ProductImage> images = this.productImageService.getMediaByProduct(detail.getId());

                orderItem.setProductName(
                    origin.getName() + " " +
                    detail.getSpecifications() + " " +
                    Color.COLORS[detail.getColor()]);
                
                orderItem.setImage(images.get(0).getLink());
                orderItems.add(orderItem);
            }

            if (bill.getDeliveryInfoId() != 0) {
                Optional<DeliveryInfo> deliveryInfo = this.deliveryInfoService.getById(bill.getDeliveryInfoId());
                
                if (deliveryInfo != null) {
                    ShippingAddressResponse shippingAddress = this.modelMapper.map(deliveryInfo, ShippingAddressResponse.class);
                    response.setShippingAddress(shippingAddress);
                }
            } else {
                User currentUser = this.userService.getById(bill.getUserId());

                ShippingAddressResponse shippingAddress = new ShippingAddressResponse();
                shippingAddress.setAcceptorName(currentUser.getFirstName() + " " + currentUser.getLastName());
                shippingAddress.setAcceptorPhone(currentUser.getPhone());
                shippingAddress.setDeliveryAddress(currentUser.getAddress());

                response.setShippingAddress(shippingAddress);
            }

            response.setOrderItems(orderItems);
            response.setOrderDate(bill.getOrderTime());
            items.add(response);
        }

        int totalPages = totalItems/num;
        if (totalItems%num != 0){
            totalPages++;
        }

        return ResponseEntity.status(200).body(new PaginateBill(items, totalPages));


    }

    @GetMapping("/my-bills")
    public ResponseEntity<GetMyBillsResponse> getMyBills(
        @RequestParam("status") String status,
        @RequestParam("num") Integer num,
        @RequestParam("page") Integer page,
        HttpServletRequest httpServletRequest) {
        
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            return ResponseEntity.status(400).body(null);
        }

        if (status == null) {
            status = "All";
        }

        int totalItems = 0;

        List<Bill> bills = this.billService.getByUserId(account.getUserId());

        if (bills != null) {
            totalItems = bills.size();
        }

        if (page == null) {
            page = 1;
        }

        if (num == null) {
            num = totalItems;
        }

        List<Bill> paginatedBills = new ArrayList<>();

        if (status.equals("All")) {
            paginatedBills = this.billService.paginateBillsByUserId(account.getUserId(), page, num);
        } else {
            paginatedBills = this.billService.paginateBillsByUserIdAndStatus(account.getUserId(), status, page, num);
        }

        List<BillResponse> billResponse = new ArrayList<>();

        for (Bill bill : paginatedBills) {
            BillResponse response = this.modelMapper.map(bill, BillResponse.class);
            List<Order> orders = this.orderService.getByBillId(bill.getId());

            List<OrderItem> orderItems = new ArrayList<>();

            for (Order order : orders) {
                OrderItem orderItem = this.modelMapper.map(order, OrderItem.class);
                ProductDetail detail = this.productDetailService.getById(order.getProductId());
                Product origin = this.productService.getById(detail.getProductId());
                List<ProductImage> images = this.productImageService.getMediaByProduct(detail.getId());

                orderItem.setProductName(
                    origin.getName() + " " +
                    detail.getSpecifications() + " " +
                    Color.COLORS[detail.getColor()]);
                
                orderItem.setImage(images.get(0).getLink());
                orderItems.add(orderItem);
            }

            if (bill.getDeliveryInfoId() != 0) {
                Optional<DeliveryInfo> deliveryInfo = this.deliveryInfoService.getById(bill.getDeliveryInfoId());
                
                if (deliveryInfo != null) {
                    ShippingAddressResponse shippingAddress = this.modelMapper.map(deliveryInfo, ShippingAddressResponse.class);
                    response.setShippingAddress(shippingAddress);
                }
            } else {
                User currentUser = this.userService.getById(account.getUserId());

                ShippingAddressResponse shippingAddress = new ShippingAddressResponse();
                shippingAddress.setAcceptorName(currentUser.getFirstName() + " " + currentUser.getLastName());
                shippingAddress.setAcceptorPhone(currentUser.getPhone());
                shippingAddress.setDeliveryAddress(currentUser.getAddress());

                response.setShippingAddress(shippingAddress);
            }
            response.setOrderItems(orderItems);
            response.setOrderDate(bill.getOrderTime());
            billResponse.add(response);
        }

        int totalPage = totalItems / num;

        if (totalItems % num != 0) {
            totalPage++;
        }

        return ResponseEntity.status(200).body(new GetMyBillsResponse(billResponse, totalPage, num));
    }
    
    @PutMapping("/edit")
    public ResponseEntity<EditBillResponse> edit(
        @RequestBody EditBillRequest request,
        HttpServletRequest httpServletRequest) {
        
        EditBillResponse response = new EditBillResponse();
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            return ResponseEntity.status(400).body(null);
        }
    
        Bill bill = this.billService.getById(request.getId());

        if (bill == null) {
            response.setMessage("Bil not found.");
            
            return ResponseEntity.status(400).body(response);
        }

        if (!bill.getUserId().equals(account.getUserId())) {
            if (!account.getRole().equals("admin")) {
                response.setMessage("You are not the owner of this bill.");
                
                return ResponseEntity.status(403).body(response);
            }
        }
        
        BillResponse billResponse = this.modelMapper.map(bill, BillResponse.class);

        if (bill.getPaymentType().equals("cash") && bill.getStatus().equals("Processing")) {
            List<Order> orders = this.orderService.getByBillId(bill.getId());

            for (Order order : orders) {
                Warehouse warehouse = this.warehouseService.getByProductId(order.getProductId());
                if (request.getStatus().equals("Cancelled")) {
                    warehouseHandle(warehouse, order.getQuantity(), request.getStatus());
                }

                ProductDetail productDetail = this.productDetailService.getById(order.getProductId());

                productDetail.setTotalSales(productDetail.getTotalSales() - order.getQuantity());
                this.productDetailService.edit(productDetail);
            }
        }

        List<Order> orders = this.orderService.getByBillId(bill.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (Order order : orders) {
            OrderItem orderItem = this.modelMapper.map(order, OrderItem.class);
            ProductDetail detail = this.productDetailService.getById(order.getProductId());
            Product origin = this.productService.getById(detail.getProductId());
            List<ProductImage> images = this.productImageService.getMediaByProduct(detail.getId());

            orderItem.setProductName(
                origin.getName() + " " +
                detail.getSpecifications() + " " +
                Color.COLORS[detail.getColor()]);
            
            orderItem.setImage(images.get(0).getLink());
            orderItems.add(orderItem);
        }

        if (!bill.getStatus().equals("Paid")) {
            bill.setStatus(request.getStatus());
            this.billService.edit(bill);
        }

        if (bill.getDeliveryInfoId() != 0) {
            Optional<DeliveryInfo> deliveryInfo = this.deliveryInfoService.getById(bill.getDeliveryInfoId());
            
            if (deliveryInfo != null) {
                ShippingAddressResponse shippingAddress = this.modelMapper.map(deliveryInfo, ShippingAddressResponse.class);
                billResponse.setShippingAddress(shippingAddress);
            }
        } else {
            User currentUser = this.userService.getById(account.getUserId());

            ShippingAddressResponse shippingAddress = new ShippingAddressResponse();
            shippingAddress.setAcceptorName(currentUser.getFirstName() + " " + currentUser.getLastName());
            shippingAddress.setAcceptorPhone(currentUser.getPhone());
            shippingAddress.setDeliveryAddress(currentUser.getAddress());

            billResponse.setShippingAddress(shippingAddress);
        }

        billResponse.setStatus(bill.getStatus());
        billResponse.setOrderItems(orderItems);
        billResponse.setOrderDate(bill.getOrderTime());

        response.setBill(billResponse);
        response.setSuccess(true);

        return ResponseEntity.status(200).body(response);
    }

    private void warehouseHandle(Warehouse warehouse, int quantityGotten, String status) {
        WarehouseHistory record = new WarehouseHistory();

        record.setWarehouseId(warehouse.getId());
        record.setQuantity(quantityGotten);

        if (!status.equals("Cancelled")) {
            warehouse.setQuantity(warehouse.getQuantity() - quantityGotten);
            this.warehouseService.edit(warehouse);
            record.setType(Enums.TypeWarehouse.MINUS.toString());
        } else {
            warehouse.setQuantity(warehouse.getQuantity() + quantityGotten);
            this.warehouseService.edit(warehouse);
            record.setType(Enums.TypeWarehouse.PLUS.toString());
        }

        this.warehouseHistoryService.save(record);
    }
}
