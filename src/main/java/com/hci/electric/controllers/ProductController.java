package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.product.AddProductRequest;
import com.hci.electric.dtos.product.AddProductResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Product;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final Jwt jwt;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final DiscountService discountService;
    private final WarehouseService warehouseService;

    public ProductController(ProductService productService, AccountService accountService, DiscountService discountService, WarehouseService warehouseService){
        this.productService = productService;
        this.accountService = accountService;
        this.discountService = discountService;
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
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString().toLowerCase()) == false){
            return ResponseEntity.status(403).body(new AddProductResponse(false, "Forbidden", null));
        }


        Product product = this.modelMapper.map(request, Product.class);

        Product savedProduct = this.productService.save(product);
        if(savedProduct == null){
            return ResponseEntity.status(400).body(new AddProductResponse(false, "Cannot Save Product", null));
        }

        Discount discount = new Discount();
        discount.setProductId(savedProduct.getId());
        discount.setValue(0.0);
        this.discountService.save(discount);

        Warehouse warehouse = new Warehouse();
        warehouse.setProductId(savedProduct.getId());
        warehouse.setQuantity(0);
        this.warehouseService.save(warehouse);

        return ResponseEntity.status(200).body(new AddProductResponse(true, "Saved Product", savedProduct));
    }



    
}
