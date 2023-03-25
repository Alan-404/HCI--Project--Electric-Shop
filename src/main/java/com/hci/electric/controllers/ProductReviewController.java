package com.hci.electric.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.review.AddReviewRequest;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.models.Order;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductReview;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductReviewService;

@RestController
@RequestMapping("/review")
public class ProductReviewController {
    private final ProductReviewService productReviewService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final BillService billService;
    private final ProductDetailService productDetailService;
    private final Auth auth;

    public ProductReviewController(ProductReviewService productReviewService, AccountService accountService, OrderService orderService, BillService billService, ProductDetailService productDetailService){
        this.productReviewService = productReviewService;
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.billService = billService;
        this.orderService = orderService;

        this.auth = new Auth(this.accountService);
        
    }

    @PostMapping("/api")
    public ResponseEntity<ProductReview> addReview(HttpServletRequest httpServletRequest, @RequestBody AddReviewRequest review){
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        Order order = this.orderService.getById(review.getOrderId());
        if (order == null || order.isReviewed() == true){
            return ResponseEntity.status(400).body(null);
        }


        Bill bill = this.billService.getById(order.getBillId());
        if (bill ==null || bill.getUserId().equals(account.getUserId()) == false){
            return ResponseEntity.status(400).body(null);
        }

        ProductDetail item = this.productDetailService.getById(order.getProductId());
        if (item == null){
            return ResponseEntity.status(400).body(null);
        }

        ProductReview record = new ProductReview();
        record.setUserId(account.getUserId());
        record.setContent(review.getContent());
        record.setProductId(item.getProductId());
        record.setStars(review.getStars());
        

        ProductReview savedReview = this.productReviewService.save(record);
        if (savedReview == null){
            return ResponseEntity.status(500).body(null);
        }

        this.orderService.saveRecord(order, true);

        return ResponseEntity.status(200).body(savedReview);
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductReview>> getReviews(@RequestParam("id") String productId){
        List<ProductReview> reviews = this.productReviewService.getByProduct(productId);

        return ResponseEntity.status(200).body(reviews);
    }
}
