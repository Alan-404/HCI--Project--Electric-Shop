package com.hci.electric.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.product.AddProductRequest;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Product;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final Jwt jwt;
    private final AccountService accountService;

    public ProductController(ProductService productService, AccountService accountService){
        this.productService = productService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }



    
}
