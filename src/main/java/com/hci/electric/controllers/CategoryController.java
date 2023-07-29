package com.hci.electric.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.category.HandleCategoryResponse;
import com.hci.electric.dtos.common.DeleteResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Category;
import com.hci.electric.models.ProductCategory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CategoryService;
import com.hci.electric.services.ProductCategoryService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final ProductCategoryService productCategoryService;

    private final Auth auth;

    public CategoryController(
        CategoryService categoryService,
        AccountService accountService,
        ProductCategoryService productCategoryService){
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.productCategoryService = productCategoryService;

        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<HandleCategoryResponse> addCategory(@RequestBody Category request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString().toLowerCase()) == false){
            return ResponseEntity.status(403).body(new HandleCategoryResponse(false, "Forbidden", null));
        }

        Category savedCategory = this.categoryService.save(request);
        if (savedCategory == null){
            return ResponseEntity.status(500).body(new HandleCategoryResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new HandleCategoryResponse(true, "Saved Category", savedCategory));
    }

    @PutMapping("/api")
    public ResponseEntity<HandleCategoryResponse> editCategory(@RequestBody Category request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString().toLowerCase()) == false){
            return ResponseEntity.status(403).body(new HandleCategoryResponse(false, "Forbidden", null));
        }

        Category savedCategory = this.categoryService.edit(request);
        if (savedCategory == null){
            return ResponseEntity.status(500).body(new HandleCategoryResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new HandleCategoryResponse(true, "Edited Category", savedCategory));
    }
    
    @GetMapping("/api")
    public List<Category> getAll(@RequestParam(name = "num", required = false) Integer num, @RequestParam(name = "page", required = false) Integer page){
        if (num == null){
            num = this.categoryService.getAll().size();
        }

        if (page == null){
            page = 1;
        }

        List<Category> categories = this.categoryService.paginate(num, page);
        return categories;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteResponse> detete(
        @PathVariable("id") String id,
        HttpServletRequest httpServletRequest) {
        DeleteResponse response = new DeleteResponse();

        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);

        if (account == null) {
            response.setMessage("You are not log in.");
            return ResponseEntity.status(401).body(response);
        }

        if (!account.getRole().toLowerCase().equals("admin")) {
            response.setMessage("You don't have permission to delete this resource.");
            return ResponseEntity.status(403).body(response);
        }

        if (id == null) {
            response.setMessage("Please specify a category.");
            return ResponseEntity.status(400).body(response);
        }

        List<ProductCategory> pcList = this.productCategoryService.getByCategoryId(id);

        if (pcList.size() > 0) {
            response.setMessage("This category already have products");
            return ResponseEntity.status(400).body(response);
        }

        String deletedId = this.categoryService.delete(id);

        if (deletedId == null) {
            response.setMessage("Internal Server Error.");
            return ResponseEntity.status(500).body(response);
        }

        response.setStatus(true);
        response.setMessage("Delete successfully.");
        response.setId(deletedId);

        return ResponseEntity.status(200).body(response);
    }
}
