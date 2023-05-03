package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.productFavorite.CreateFavoriteRequest;
import com.hci.electric.dtos.productFavorite.ProductFavoriteResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductFavorite;
import com.hci.electric.models.ProductImage;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductFavoriteService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.utils.Color;


@RestController
@RequestMapping("/favorite")
public class ProductFavoriteController {
    private ProductFavoriteService productFavoriteService;
    private AccountService accountService;
    private Auth auth;
    private ProductService productService;
    private final ProductDetailService productDetailService;
    private final ProductImageService productImageService;
    private final ModelMapper modelMapper;

    public ProductFavoriteController(
        ProductFavoriteService productFavoriteService,
        AccountService accountService,
        ProductService productService,
        ProductDetailService productDetailService,
        ProductImageService productImageService) {
        this.productFavoriteService = productFavoriteService;
        this.accountService = accountService;
        this.auth = new Auth(this.accountService);
        this.modelMapper = new ModelMapper();
        this.productService = productService;
        this.productDetailService = productDetailService;
        this.productImageService = productImageService;
    }

    @PostMapping("/api")
    public ResponseEntity<ProductFavoriteResponse> addProductFavorite(@RequestBody CreateFavoriteRequest request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        ProductDetail product = this.productDetailService.getById(request.getProductId());
        if (product == null) {
            return ResponseEntity.status(400).body(null);
        }

        ProductFavorite favorite = new ProductFavorite();
        favorite.setProductId(product.getId());
        favorite.setUserId(account.getUserId());

        ProductFavorite savedFavorite = this.productFavoriteService.save(favorite);

        if (savedFavorite == null) {
            return ResponseEntity.status(500).body(null);
        }

        ProductFavoriteResponse response = this.modelMapper.map(savedFavorite, ProductFavoriteResponse.class);
        Product origin = this.productService.getById(product.getProductId());
        List<ProductImage> images = this.productImageService.getMediaByProduct(product.getId());
        int totalHeart = this.productFavoriteService.countByProduct(savedFavorite.getProductId());

        response.setProductName(
            origin.getName() + " " +
            product.getSpecifications() + " " +
            Color.COLORS[product.getColor()]
        );
        response.setImage(images.get(0).getLink());
        response.setTotalHeart(totalHeart);

        return ResponseEntity.status(200).body(response);
    }


    @GetMapping("/my")
    public ResponseEntity<List<ProductFavoriteResponse>> getByUser(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        List<ProductFavorite> favorites = this.productFavoriteService.getByUser(account.getUserId());
        List<ProductFavoriteResponse> responses = new ArrayList<>();

        for (ProductFavorite favorite : favorites) {
            ProductFavoriteResponse response = this.modelMapper.map(favorite, ProductFavoriteResponse.class);
            ProductDetail productDetail = this.productDetailService.getById(favorite.getProductId());
            Product productOrigin = this.productService.getById(productDetail.getProductId());
            List<ProductImage> images = this.productImageService.getMediaByProduct(productDetail.getId());
            int totalHeart = this.productFavoriteService.countByProduct(productDetail.getId());

            response.setProductName(
                productOrigin.getName() + " " +
                productDetail.getSpecifications() + " " +
                Color.COLORS[productDetail.getColor()]
            );

            response.setImage(images.get(0).getLink());
            response.setTotalHeart(totalHeart);
            responses.add(response);
        }

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Integer> remove(@PathVariable("id") Integer id) {
        this.productFavoriteService.delete(id);

        return ResponseEntity.status(200).body(id);
    }
}
