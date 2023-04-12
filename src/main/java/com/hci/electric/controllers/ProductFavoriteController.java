package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductFavorite;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.ProductFavoriteService;
import com.hci.electric.services.ProductService;


@RestController
@RequestMapping("/favorite")
public class ProductFavoriteController {
    private ProductFavoriteService productFavoriteService;
    private AccountService accountService;
    private Auth auth;
    private ProductService productService;

    public ProductFavoriteController(ProductFavoriteService productFavoriteService, AccountService accountService, ProductService productService){
        this.productFavoriteService = productFavoriteService;
        this.accountService = accountService;
        this.auth = new Auth(this.accountService);
        this.productService = productService;

    }

    @PostMapping("/api")
    public ResponseEntity<Boolean> addProductFavorite(@RequestBody String productId, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(400).body(false);
        }

        Product product = this.productService.getById(productId);
        if (product == null){
            return ResponseEntity.status(400).body(false);
        }

        ProductFavorite favorite = new ProductFavorite();
        favorite.setProductId(productId);
        favorite.setUserId(account.getUserId());

        if (this.productFavoriteService.save(favorite) == null){
            return ResponseEntity.status(500).body(false);
        }

        return ResponseEntity.status(200).body(true);
    }
}
