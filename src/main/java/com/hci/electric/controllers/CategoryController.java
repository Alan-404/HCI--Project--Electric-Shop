package com.hci.electric.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.category.HandleCategoryResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Category;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CategoryService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final Jwt jwt;

    public CategoryController(CategoryService categoryService, AccountService accountService){
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    @PostMapping("/api")
    public ResponseEntity<HandleCategoryResponse> addCategory(@RequestBody Category request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new HandleCategoryResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new HandleCategoryResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
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
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new HandleCategoryResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new HandleCategoryResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
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


}
