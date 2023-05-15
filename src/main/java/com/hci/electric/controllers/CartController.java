package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.util.WebUtils;

import com.hci.electric.dtos.cart.CartItemResponse;
import com.hci.electric.dtos.cart.CartResponse;
import com.hci.electric.dtos.cart.CheckAllRequest;
import com.hci.electric.dtos.cart.CheckItemRequest;
import com.hci.electric.dtos.cart.PaginateCartItems;
import com.hci.electric.dtos.cart.RemoveItemRequest;
import com.hci.electric.dtos.cart.RemoveMultiItemsRequest;
import com.hci.electric.dtos.cart.UpdateCartItemRequest;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Cart;
import com.hci.electric.models.CartItem;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CartItemService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseService;


@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final CartItemService cartItemService;
    private final DiscountService discountService;
    private final ProductImageService productImageService;
    private final Auth auth;

    public CartController(CartService cartService,
        AccountService accountService,
        WarehouseService warehouseService,
        ProductService productService,
        ProductDetailService productDetailService,
        CartItemService cartItemService,
        DiscountService discountService,
        ProductImageService productImageService) {
        this.cartService = cartService;
        this.modelMapper = new ModelMapper();
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.discountService = discountService;
        this.productImageService = productImageService;
        this.auth = new Auth(this.accountService);
    }

    public boolean checkWarehouse(String productId, int quantity){
        Warehouse warehouse = this.warehouseService.getByProductId(productId);
        if (warehouse == null || warehouse.getQuantity() == 0 || warehouse.getQuantity() < quantity){
            return false;
        }
        return true;
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addToCart(@RequestBody CartItem item, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);

        if(account == null){
            return ResponseEntity.status(400).body(false);
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart ==null){
            cart = new Cart();
            cart.setUserId(account.getUserId());
            cart = this.cartService.save(cart);
        }

        CartItem checkItem = this.cartItemService.getByCartIdAndProduct(cart.getId(), item.getProductId());
        item.setCartId(cart.getId());
        if (checkItem == null){
            if (this.checkWarehouse(item.getProductId(), item.getQuantity()) == false){
                return ResponseEntity.status(400).body(false);
            }
        
            CartItem savedCartItem = this.cartItemService.save(item);
            if (savedCartItem == null){
                return ResponseEntity.status(500).body(false);
            }
            return ResponseEntity.status(200).body(true);
        }
        else{
            if (this.checkWarehouse(item.getProductId(), item.getQuantity() + checkItem.getQuantity()) == false){
                return ResponseEntity.status(400).body(false);
            }
            item.setId(checkItem.getId());
            item.setCreatedAt(checkItem.getCreatedAt());
            item.setQuantity(item.getQuantity() + checkItem.getQuantity());
            CartItem savedCartItem = this.cartItemService.edit(item);
            if (savedCartItem == null){
                return ResponseEntity.status(500).body(false);
            }
            return ResponseEntity.status(200).body(true);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<Boolean> editQuantityCartItem(HttpServletRequest httpServletRequest, @RequestBody CartItem item){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(400).body(false);
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart == null){
            return ResponseEntity.status(400).body(false);
        }

        CartItem checkItem = this.cartItemService.getByCartIdAndProduct(cart.getId(), item.getProductId());
        if (checkItem == null){
            return ResponseEntity.status(400).body(false);
        }

        if (this.checkWarehouse(item.getProductId(), item.getQuantity()) == false){
            return ResponseEntity.status(400).body(false);
        }

        checkItem.setQuantity(item.getQuantity());
        checkItem = this.cartItemService.edit(checkItem);
        if (checkItem == null){
            return ResponseEntity.status(500).body(false);
        }
        return ResponseEntity.status(200).body(true);
    }


    @PutMapping("/status/{id}")
    public ResponseEntity<Boolean> changeStatusProductInCart(HttpServletRequest httpServletRequest, @PathVariable("id") String productId){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);

        if (account == null){
            return ResponseEntity.status(400).body(false);
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart == null){
            return ResponseEntity.status(400).body(false);
        }

        CartItem item = this.cartItemService.getByCartIdAndProduct(cart.getId(), productId);
        if (item == null){
            return ResponseEntity.status(400).body(false);
        }

        item.setStatus(!item.isStatus());
        CartItem savedItem = this.cartItemService.edit(item);
        if (savedItem == null){
            return ResponseEntity.status(400).body(false);
        }

        return ResponseEntity.status(200).body(true);
    }
    


    @PutMapping("/status_all")
    public ResponseEntity<Boolean> changeStatusAllInCart(HttpServletRequest httpServletRequest, @RequestParam(required = false) Boolean status){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);

        if (account == null){
            return ResponseEntity.status(400).body(false);
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart == null){
            return ResponseEntity.status(400).body(false);
        }
        if (status == null){
            status = true;
        }

        if (this.cartItemService.changeStatusInCart(cart.getId(), status) == false){
            return ResponseEntity.status(400).body(false);
        }

        return ResponseEntity.status(200).body(true);
    }

    @GetMapping("/my")
    public ResponseEntity<PaginateCartItems> getMyCart(HttpServletRequest httpServletRequest, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer num){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(400).body(new PaginateCartItems(new ArrayList<>(), 0));
        }

        if (page == null){
            page = 1;
        }

        Cart cart = this.cartService.getByUserId(account.getUserId());
        if (cart == null){
            return ResponseEntity.status(400).body(new PaginateCartItems(new ArrayList<>(), 0));
        }

        int totalItems = this.cartItemService.getAllItemsByCart(cart.getId()).size();
        if (num == null){
            num = totalItems;
        }


        int totalPages = totalItems/num;
        if (totalItems%num != 0){
            totalPages++;
        }

        List<CartItem> items = this.cartItemService.paginateByCartId(cart.getId(), page, num);

        if (items == null){
            return ResponseEntity.status(500).body(new PaginateCartItems(new ArrayList<>(), 0));
        }

        return ResponseEntity.status(200).body(new PaginateCartItems(items, totalPages));
    }

    @GetMapping("/user")
    public ResponseEntity<CartResponse> getCurrentUserCart(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {
        
        String userId = null;
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "userId");
        
        if (cookie != null) {
            userId = cookie.getValue();
        }

        String username = userId;
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);

        if (account == null) {
            if (userId == null) {
                UUID annonymousUser = UUID.randomUUID();
                Cookie userCookie = new Cookie("userId", annonymousUser.toString());

                userCookie.setMaxAge(180 * 24 * 60 * 60);
                userCookie.setHttpOnly(true);
                userCookie.setPath("/");
                httpServletResponse.addCookie(userCookie);

                username = annonymousUser.toString();
            }
        } else {
           username = account.getUserId();
        }

        System.out.println("Username.....................: " + username);

        CartResponse cartResponse = getOrCreateCartForUser(username);

        return ResponseEntity.status(200).body(cartResponse);
    }

    @PutMapping("/check-item")
    public ResponseEntity<CartResponse> checkItem(
        @RequestBody CheckItemRequest request,
        HttpServletRequest httpServletRequest) {
        
        Cart cart = this.cartService.getById(request.getCartId());

        if (cart == null) {
            return ResponseEntity.status(400).body(null);
        }

        CartItem item = this.cartItemService.getByCartIdAndProduct(cart.getId(), request.getProductId());

        if (item == null) {
            return ResponseEntity.status(400).body(null);
        }

        item.setStatus(request.isChecked());
        this.cartItemService.edit(item);

        CartResponse cartResponse = this.map(cart);

        return ResponseEntity.status(200).body(cartResponse);
    }

    @PutMapping("/check-all")
    public ResponseEntity<CartResponse> checkItem(
        @RequestBody CheckAllRequest request,
        HttpServletRequest httpServletRequest) {
        
        Cart cart = this.cartService.getById(request.getCartId());

        if (cart == null) {
            return ResponseEntity.status(400).body(null);
        }

        List<CartItem> items = this.cartItemService.getAllItemsByCart(cart.getId());

        for (CartItem item : items) {
            item.setStatus(request.isChecked());
            this.cartItemService.edit(item);
        }

        CartResponse cartResponse = this.map(cart);

        return ResponseEntity.status(200).body(cartResponse);
    }

    @PutMapping("/remove-item")
    public ResponseEntity<CartResponse> removeItem(
        @RequestBody RemoveItemRequest request,
        HttpServletRequest httpServletRequest) {

        Cart cart = this.cartService.getById(request.getCartId());

        if (cart == null) {
            return ResponseEntity.status(400).body(null);
        }

        CartItem item = this.cartItemService.getByCartIdAndProduct(cart.getId(), request.getProductId());

        if (item == null) {
            return ResponseEntity.status(400).body(null);
        }

        this.cartItemService.delete(item);

        CartResponse cartResponse = this.map(cart);

        return ResponseEntity.status(200).body(cartResponse);
    }

    @PutMapping("/remove-multi-items")
    public ResponseEntity<CartResponse> removeMultiItems(
        @RequestBody RemoveMultiItemsRequest request,
        HttpServletRequest httpServletRequest) {

        Cart cart = this.cartService.getById(request.getCartId());

        if (cart == null) {
            return ResponseEntity.status(400).body(null);
        }

        for (String productId : request.getProductIds()) {
            CartItem item = this.cartItemService.getByCartIdAndProduct(request.getCartId(), productId);
            this.cartItemService.delete(item);
        }

        CartResponse cartResponse = this.map(cart);

        return ResponseEntity.status(200).body(cartResponse);
    }

    @PutMapping("/update-cart-item")
    public ResponseEntity<CartResponse> addToCart(
        @RequestBody UpdateCartItemRequest request,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {

        String accessToken = httpServletRequest.getHeader("Authorization");
        String userId = null;
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "userId");
        
        if (cookie != null) {
            userId = cookie.getValue();
        }

        String user = this.getOrSetCartCookieAndUserId(accessToken, userId, httpServletResponse);
        ProductDetail product = this.productDetailService.getById(request.getProductId());

        if (product == null) {
            return ResponseEntity.status(400).body(null);
        }

        Cart cart = this.addItemToCart(user, product.getId(), request.getQuantity());   
        CartResponse cartResponse = this.map(cart);

        return ResponseEntity.status(200).body(cartResponse);
        
    }

    private Cart addItemToCart(String userId, String productId, int quantity) {
        Cart cart = this.cartService.getByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);

            this.cartService.save(cart);

            CartItem item = new CartItem();
    
            item.setCartId(cart.getId());
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setStatus(false);
    
            this.cartItemService.save(item);
            
            return cart;
        }

        List<CartItem> items = this.cartItemService.getAllItemsByCart(cart.getId());

        boolean isNewItem = true;
        for (CartItem item : items) {
            if (item.getProductId().equals(productId) ) {
                item.setQuantity(item.getQuantity() + quantity);
                this.cartItemService.edit(item);
                
                isNewItem = false;
                break;
            }
        }

        if (isNewItem) {
            CartItem item = new CartItem();
            item.setCartId(cart.getId());
            item.setProductId(productId);
            item.setQuantity(quantity);
            
            this.cartItemService.save(item);
        }

        return cart;
    }

    private CartResponse getOrCreateCartForUser(String userId) {
        Cart cart = this.cartService.getByUserId(userId);

        if (cart == null) {
            return createCartForUser(userId);
        }

        return this.map(cart);
    }


    private CartResponse createCartForUser(String userId) {
        Cart cart = new Cart();

        cart.setUserId(userId);

        this.cartService.save(cart);

        return this.modelMapper.map(cart, CartResponse.class);
    }

    private CartResponse map(Cart cart) {
        List<CartItem> items = this.cartItemService.getAllItemsByCart(cart.getId());
        CartResponse cartResponse = this.modelMapper.map(cart, CartResponse.class);
        List<CartItemResponse> itemResponses = new ArrayList<>();

        for (CartItem item : items) {
            CartItemResponse itemResponse = this.modelMapper.map(item, CartItemResponse.class);
            ProductDetail productDetail = this.productDetailService.getById(item.getProductId());
            
            if (productDetail == null) {
                continue;
            }

            Product productOrigin = this.productService.getById(productDetail.getProductId());
            Discount discount = this.discountService.getByProductId(productDetail.getId());
            Warehouse warehouse = this.warehouseService.getByProductId(productDetail.getId());
            List<ProductImage> images = this.productImageService.getMediaByProduct(productDetail.getId());
            List<String> imageUrls = new ArrayList<>();

            for (ProductImage image : images) {
                imageUrls.add(image.getLink());
            }

            itemResponse.setPrice(productDetail.getPrice());
            itemResponse.setProductName(productOrigin.getName() + " " + productDetail.getSpecifications());
            itemResponse.setDiscount(discount.getValue());
            itemResponse.setImages(imageUrls);
            itemResponse.setWarehouse(warehouse.getQuantity());
            
            itemResponses.add(itemResponse);
        }

        cartResponse.setCartItems(itemResponses);

        return cartResponse;
    }

    private String getOrSetCartCookieAndUserId(
        String accessToken,
        String userIdCookie,
        HttpServletResponse httpServletResponse) {

        Account account = this.auth.checkToken(accessToken);

        if (account == null) {
            if (userIdCookie == null || userIdCookie == "") {
                UUID anonymousUser = UUID.randomUUID();
                Cookie cookie = new Cookie("userId", anonymousUser.toString());

                cookie.setMaxAge(180 * 24 * 60 * 60);
                cookie.setHttpOnly(true);
                httpServletResponse.addCookie(cookie);

                return anonymousUser.toString();
            } else {
                return userIdCookie;
            }
        } else {
            return account.getUserId();
        }
    }
}
