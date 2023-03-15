package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.cart.CartItem;
import com.hci.electric.dtos.cart.HandleCartResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Cart;
import com.hci.electric.models.Product;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseService;


@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final Jwt jwt;
    private final AccountService accountService;
    private final WarehouseService warehouseService;
    private final ProductService productService;

    public CartController(CartService cartService, AccountService accountService, WarehouseService warehouseService, ProductService productService){
        this.cartService = cartService;
        this.accountService = accountService;
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.jwt = new Jwt();
    }

    @PostMapping("/add")
    public ResponseEntity<HandleCartResponse> addProduct(@RequestBody Cart cart, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        System.out.println(accessToken);
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);

        if (accountId == null){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
        if (account == null){
            return ResponseEntity.status(404).body(new HandleCartResponse(false, "Not Found User", null));
        }

        Product product = this.productService.getById(cart.getProductId());
        if (product == null || product.isStatus() == false){
            return ResponseEntity.status(404).body(new HandleCartResponse(false, "Product has been discontinued", null));
        }

        Warehouse warehouse = this.warehouseService.getByProductId(cart.getProductId());
        if (warehouse == null || warehouse.getQuantity() < cart.getQuantity()){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Product is Out of Stock", null));
        }

        cart.setUserId(account.getUserId());
        Cart savedCart = this.cartService.save(cart);
        if (savedCart == null){
            return ResponseEntity.status(500).body(new HandleCartResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new HandleCartResponse(true, "Add Product Successfully", savedCart));
    }

    @GetMapping("/api")
    public ResponseEntity<List<CartItem>> getCartByToken(HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new ArrayList<>());
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new ArrayList<>());
        }

        Account account = this.accountService.getById(accountId);
        if (account == null || account.getStatus() == false){
            return ResponseEntity.status(404).body(new ArrayList<>());
        }

        List<Cart> productsCart = this.cartService.getByUserId(account.getUserId());
        if (productsCart == null){
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
        List<CartItem> items = new ArrayList<>();

        for (Cart product : productsCart) {
            CartItem item = new CartItem();
            item.setCart(product);
            item.setProduct(this.productService.getById(product.getProductId()));

            items.add(item);
        }

        return ResponseEntity.status(200).body(items);
    }
}
