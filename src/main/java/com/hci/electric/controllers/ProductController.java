package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.product.AddProductRequest;
import com.hci.electric.dtos.product.AddProductResponse;
import com.hci.electric.dtos.product.ProductInfor;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Distributor;
import com.hci.electric.models.Order;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductReview;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.DistributorService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductReviewService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseService;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final AccountService accountService;
    private final DistributorService distributorService;
    private final ModelMapper modelMapper;
    private final DiscountService discountService;
    private final WarehouseService warehouseService;
    private final ProductDetailService productDetailService;
    private final OrderService orderService;
    private final ProductReviewService productReviewService;
    private final Auth auth;

    public ProductController(ProductService productService, AccountService accountService, DiscountService discountService, WarehouseService warehouseService, DistributorService distributorService, ProductDetailService productDetailService, OrderService orderService, ProductReviewService productReviewService){
        this.productService = productService;
        this.accountService = accountService;
        this.orderService = orderService;
        this.productDetailService = productDetailService;
        this.discountService = discountService;
        this.distributorService = distributorService;
        this.warehouseService = warehouseService;
        this.productReviewService = productReviewService;
        this.modelMapper = new ModelMapper();

        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        
        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(403).body(new AddProductResponse(false, "Forbidden", null));
        }

        Distributor distributor = this.distributorService.getByUserId(account.getUserId());
        if (distributor == null){
            return ResponseEntity.status(500).body(new AddProductResponse(false, "You are not a Distributor now", null));
        }

        Product product = this.modelMapper.map(request, Product.class);
        product.setDistributorId(distributor.getId());
        Product savedProduct = this.productService.save(product);
        if(savedProduct == null){
            return ResponseEntity.status(500).body(new AddProductResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new AddProductResponse(true, "Saved Product", savedProduct));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductInfor> getProductById(@PathVariable("id") String id){
        if (id == null){
            return ResponseEntity.status(400).body(null);
        }

        Product product = this.productService.getById(id);
        if (product == null){
            return ResponseEntity.status(404).body(null);
        }

        List<ProductDetail> items = this.productDetailService.getByProductId(id);
        if (items == null){
            return ResponseEntity.status(404).body(null);
        }


        ProductInfor response = new ProductInfor();

        Distributor distributor = this.distributorService.getById(product.getDistributorId());
        response.setDistributor(distributor);

        response.setProduct(product);
        response.setItems(items);

        List<Discount> lstDiscounts = new ArrayList<>();
        List<Warehouse> lstWarehouse = new ArrayList<>();

        for (ProductDetail item : items) {
            lstDiscounts.add(this.discountService.getByProductId(item.getId()));
            lstWarehouse.add(this.warehouseService.getByProductId(item.getId()));
        }

        List<ProductDetail> subProducts = this.productDetailService.getByProductId(id);
        int purchases = 0;
        double star = 0;

        
        for (ProductDetail subProduct : subProducts) {
            List<Order> orders = this.orderService.getByProductId(subProduct.getId());
            if (orders == null || orders.size() == 0){
                continue;
            }

            purchases += orders.stream().mapToInt(o -> o.getQuantity()).sum();
            

        }

        List<ProductReview> reviews = this.productReviewService.getByProduct(id);
        star += reviews.stream().mapToDouble(o -> o.getStars()).sum();

        response.setDiscounts(lstDiscounts);
        response.setWarehouses(lstWarehouse);
        response.setPurchases(purchases);
        response.setReviews(reviews.size());
        response.setStar(Double.valueOf(star/(reviews.size())));

        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/api")
    public ResponseEntity<AddProductResponse> editProduct(@RequestBody Product product, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        
        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "Invalid User", null));
        }

        Distributor distributor = this.distributorService.getByUserId(account.getUserId());
        if (distributor == null){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "You are not Distributor", null));
        }

        Product checkProduct = this.productService.getById(product.getId());
        if (checkProduct == null || checkProduct.getDistributorId().equals(distributor.getId()) == false){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "You are not Owner of Product", null));
        }

        product.setCreatedAt(checkProduct.getCreatedAt());

        Product savedProduct = this.productService.edit(product);
        if (savedProduct == null){
            return ResponseEntity.status(500).body(new AddProductResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new AddProductResponse(true, "Edit Product Successfully", savedProduct));
    }
    
}
