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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.cart.CartItem;
import com.hci.electric.dtos.cart.HandleCartResponse;
import com.hci.electric.dtos.cart.PaginationCartItems;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Cart;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseService;


@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final Jwt jwt;
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final ProductDetailService productDetailService;

    public CartController(CartService cartService, AccountService accountService, WarehouseService warehouseService, ProductService productService, ProductDetailService productDetailService){
        this.cartService = cartService;
        this.modelMapper = new ModelMapper();
        this.productDetailService = productDetailService;
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

        ProductDetail product = this.productDetailService.getById(cart.getProductId());
        if (product == null){
            return ResponseEntity.status(404).body(new HandleCartResponse(false, "Product has been discontinued", null));
        }

        Warehouse warehouse = this.warehouseService.getByProductId(cart.getProductId());
        if (warehouse == null || warehouse.getQuantity() < cart.getQuantity()){
            return ResponseEntity.status(200).body(new HandleCartResponse(false, "Product is Out of Stock", null));
        }
        cart.setUserId(account.getUserId());
        Cart item = this.cartService.getByUserAndProduct(account.getUserId(), product.getId());
        Cart savedCart;
        if (item == null){   
            savedCart = this.cartService.save(cart);
        }
        else{
            cart = this.modelMapper.map(item, Cart.class);
            cart.setQuantity(item.getQuantity() + cart.getQuantity());
            if (warehouse.getQuantity() < cart.getQuantity()){
                return ResponseEntity.status(200).body(new HandleCartResponse(false, "Product is Out of Stock", null));
            }
            savedCart = this.cartService.edit(cart);
        }

        if (savedCart == null){
            return ResponseEntity.status(500).body(new HandleCartResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new HandleCartResponse(true, "Add Product Successfully", savedCart));
    }

    @PutMapping("/handle")
    public ResponseEntity<HandleCartResponse> handleProductCart(HttpServletRequest httpServletRequest, @RequestBody Cart cart){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
        if (account == null || account.getStatus() == false){
            return ResponseEntity.status(404).body(new HandleCartResponse(false, "User Not Found", null));
        }

        cart.setUserId(account.getUserId());

        Warehouse warehouse = this.warehouseService.getByProductId(cart.getProductId());
        if (warehouse.getQuantity() < cart.getQuantity()){
            return ResponseEntity.status(200).body(new HandleCartResponse(false, "Product is Out of Stock", null));
        }

        Cart savedCart = this.cartService.edit(cart);
        if (savedCart == null){
            return ResponseEntity.status(500).body(new HandleCartResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new HandleCartResponse(true, "Added Product to Cart", savedCart));
    } 

    @PutMapping("/status/{id}")
    public ResponseEntity<HandleCartResponse> changeStatusItem(HttpServletRequest httpServletRequest, @PathVariable("id") String cartId){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new HandleCartResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
        if (account == null || account.getStatus() == false){
            return ResponseEntity.status(404).body(new HandleCartResponse(false, "Not Found User", null));
        }

        Cart item = this.cartService.getById(cartId);
        item.setStatus(!(item.isStatus()));

        Cart savedCart = this.cartService.edit(item);

        if (savedCart == null){
            return ResponseEntity.status(500).body(new HandleCartResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new HandleCartResponse(true, "Changed Status", savedCart));
    }

    @GetMapping("/api")
    public ResponseEntity<PaginationCartItems> getCartByToken(HttpServletRequest httpServletRequest, @RequestParam(name = "num", required = false) Integer num, @RequestParam(name = "page", required = false) Integer page){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new PaginationCartItems(new ArrayList<>(), 0, 0));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new PaginationCartItems(new ArrayList<>(), 0, 0));
        }

        Account account = this.accountService.getById(accountId);
        if (account == null || account.getStatus() == false){
            return ResponseEntity.status(404).body(new PaginationCartItems(new ArrayList<>(), 0, 0));
        }

        int totalItems = this.cartService.getByUserId(account.getUserId()).size();

        if (page == null){
            page = 1;
        }
        if (num == null){
            num = totalItems;
        }

        List<Cart> productsCart = this.cartService.paginateGetByUserId(account.getUserId(), num, (page-1)*num);
        if (productsCart == null){
            return ResponseEntity.status(500).body(new PaginationCartItems(new ArrayList<>(), 0, 0));
        }

        List<CartItem> items = new ArrayList<>();
        for (Cart product : productsCart) {
            CartItem item = new CartItem();
            item.setCart(product);
            item.setProduct(this.productService.getById(product.getProductId()));

            items.add(item);
        }

        int totalPages = totalItems/num;
        if (totalItems%num != 0){
            totalPages += 1;
        }

        return ResponseEntity.status(200).body(new PaginationCartItems(items, totalPages, totalItems));
    }
}
