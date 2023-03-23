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
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Distributor;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.DistributorService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseService;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final Jwt jwt;
    private final AccountService accountService;
    private final DistributorService distributorService;
    private final ModelMapper modelMapper;
    private final DiscountService discountService;
    private final WarehouseService warehouseService;
    private final ProductDetailService productDetailService;

    public ProductController(ProductService productService, AccountService accountService, DiscountService discountService, WarehouseService warehouseService, DistributorService distributorService, ProductDetailService productDetailService){
        this.productService = productService;
        this.accountService = accountService;
        this.productDetailService = productDetailService;
        this.discountService = discountService;
        this.distributorService = distributorService;
        this.warehouseService = warehouseService;
        this.jwt = new Jwt();
        this.modelMapper = new ModelMapper();
    }

    @PostMapping("/api")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if(accountId == null){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
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

        response.setProduct(product);
        response.setItems(items);

        List<Discount> lstDiscounts = new ArrayList<>();
        List<Warehouse> lstWarehouse = new ArrayList<>();

        for (ProductDetail item : items) {
            lstDiscounts.add(this.discountService.getByProductId(item.getId()));
            lstWarehouse.add(this.warehouseService.getByProductId(item.getId()));
        }

        response.setDiscounts(lstDiscounts);
        response.setWarehouses(lstWarehouse);

        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/api")
    public ResponseEntity<AddProductResponse> editProduct(@RequestBody Product product, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(token.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
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
