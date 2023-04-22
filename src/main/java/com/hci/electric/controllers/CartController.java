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

import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Cart;
import com.hci.electric.models.CartItem;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CartItemService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.ProductDetailService;
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
    private final Auth auth;

    public CartController(CartService cartService, AccountService accountService, WarehouseService warehouseService, ProductService productService, ProductDetailService productDetailService, CartItemService cartItemService){
        this.cartService = cartService;
        this.modelMapper = new ModelMapper();
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.warehouseService = warehouseService;
        this.productService = productService;
        this.cartItemService = cartItemService;
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
}
