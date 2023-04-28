package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.bill.BillDetail;
import com.hci.electric.dtos.bill.BillInfo;
import com.hci.electric.dtos.bill.BillResponse;
import com.hci.electric.dtos.bill.GetMyBillsResponse;
import com.hci.electric.dtos.bill.OrderItem;
import com.hci.electric.dtos.bill.PaginateBill;
import com.hci.electric.dtos.productDetail.ProductItem;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.models.Order;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.UserService;
import com.hci.electric.utils.Color;

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

    public BillController(
        BillService billService,
        OrderService orderService,
        AccountService accountService,
        ProductService productService,
        ProductDetailService productDetailService,
        UserService userService,
        ProductImageService productImageService) {
        this.billService = billService;
        this.orderService = orderService;
        this.accountService = accountService;
        this.productDetailService = productDetailService;
        this.productService = productService;
        this.modelMapper = new ModelMapper();
        this.auth = new Auth(this.accountService);
        this.userService = userService;
        this.productImageService = productImageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillInfo> getBill(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        Bill bill = this.billService.getById(id);
        if (bill.getUserId().equals(account.getUserId()) == false){
            return ResponseEntity.status(403).body(null);
        } 

        List<Order> items = this.orderService.getByBillId(bill.getId());
        List<ProductItem> products = new ArrayList<>();
        
        BillInfo billDetail = new BillInfo();
        billDetail.setBill(bill);

        for (Order orderItem : items) {
            ProductItem item = new ProductItem();
            ProductDetail detail = this.productDetailService.getById(orderItem.getProductId());
            item.setDetail(detail);
            item.setProduct(this.productService.getById(detail.getProductId()));

            products.add(item);
        }

        billDetail.setItems(products);



        return ResponseEntity.status(200).body(billDetail);
    }

    @GetMapping("/api")
    public ResponseEntity<List<Bill>> getBills(@RequestParam("num") Integer num, @RequestParam("page") Integer page, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        if(page == null){
            page = 1;
        }

        int totalBills = this.billService.getByUserId(account.getUserId()).size();

        if (num == null){
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
        List<BillDetail> items = new ArrayList<>();
        for (Bill bill : bills) {
            User user = this.userService.getById(bill.getUserId());
            BillDetail item = this.modelMapper.map(bill, BillDetail.class);
            item.setFirstName(user.getFirstName());
            item.setLastName(user.getLastName());
            item.setEmail(user.getEmail());
            item.setAddress(user.getAddress());
            item.setAvatar(user.getAvatar());
            item.setPhone(user.getPhone());
            item.setBirthDate(user.getBirthDate());
            item.setGender(user.getGender());

            items.add(item);
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
    
}
