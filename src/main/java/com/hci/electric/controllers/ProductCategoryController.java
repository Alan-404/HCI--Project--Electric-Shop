package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.productCategory.AddProductCategoryRequest;
import com.hci.electric.dtos.productCategory.AddProductCategoryResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductCategory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CategoryService;
import com.hci.electric.services.ProductCategoryService;
import com.hci.electric.services.ProductService;

@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final Jwt jwt;
    private final AccountService accountService;

    public ProductCategoryController(ProductCategoryService productCategoryService, AccountService accountService, ProductService productService, CategoryService categoryService){
        this.productCategoryService = productCategoryService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.jwt = new Jwt();
    }

    public ResponseEntity<AddProductCategoryResponse> addCategoriesProduct(@RequestBody AddProductCategoryRequest request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new AddProductCategoryResponse(false, "Invalid Token", null, null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new AddProductCategoryResponse(false, "Invalid Token", null, null));
        }

        Account account = this.accountService.getById(accountId);
        if (account == null || account.getStatus() == false){
            return ResponseEntity.status(404).body(new AddProductCategoryResponse(false, "Not Found User", null, null));
        }

        Product product = this.productService.getById(request.getProductId());
        if (product == null){
            return ResponseEntity.status(404).body(new AddProductCategoryResponse(false, "Not found product", null, null));
        }
        
        boolean checkCategories = this.categoryService.checkListCategories(request.getCategories());
        if (checkCategories == false){
            return ResponseEntity.status(404).body(new AddProductCategoryResponse(false, "Found Invalid Category", null, null));
        }

        for (String category : request.getCategories()) {
            if (this.productCategoryService.save(new ProductCategory(request.getProductId(), category)) == null){
                return ResponseEntity.status(404).body(new AddProductCategoryResponse(false, "Internal Error Server", null, null));
            }
        }

        return ResponseEntity.status(200).body(new AddProductCategoryResponse(true, "Saved Categories", product, null));
    }
}
