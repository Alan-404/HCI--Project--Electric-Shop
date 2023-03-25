package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.hci.electric.dtos.productDetail.AddProductRequest;
import com.hci.electric.middlewares.Auth;
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
@RequestMapping("/detail")
public class ProductDetailController {
    private final ProductDetailService productDetailService;
    private final AccountService accountService;
    private final DistributorService distributorService;
    private final DiscountService discountService;
    private final WarehouseService warehouseService;
    private final ProductService productService;

    private final Auth auth;

    public ProductDetailController(ProductDetailService productDetailService, AccountService accountService, DistributorService distributorService, DiscountService discountService, WarehouseService warehouseService, ProductService productService){
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.productService = productService;
        this.discountService = discountService;
        this.warehouseService = warehouseService;
        this.distributorService = distributorService;

        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addDetailOfProducts(@RequestBody AddProductRequest request, HttpServletRequest httpServletRequest){
        
        String token = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        Distributor distributor = this.distributorService.getByUserId(account.getUserId());
        if (distributor == null){
            return ResponseEntity.status(400).body(null);
        }


        Product product = this.productService.getById(request.getProductId());
        if (product == null){
            return ResponseEntity.status(400).body(null);
        }

        for (int i=0; i<request.getProducts().size(); i++){
            ProductDetail item = request.getProducts().get(i);
            item.setProductId(request.getProductId());
            ProductDetail savedItem = this.productDetailService.save(item);

            
            Warehouse warehouse = new Warehouse();
            warehouse.setProductId(savedItem.getId());
            warehouse.setQuantity(request.getQuantities().get(i));
            this.warehouseService.save(warehouse);


            Discount discount = new Discount();
            discount.setProductId(savedItem.getId());
            discount.setValue(request.getDiscounts().get(i));
            this.discountService.save(discount);
        }

        return ResponseEntity.status(200).body(product);
    }
}
