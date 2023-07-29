package com.hci.electric.controllers;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.hci.electric.dtos.account.ChangePasswordRequest;
import com.hci.electric.dtos.account.LoginRequest;
import com.hci.electric.dtos.account.LoginResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Cart;
import com.hci.electric.models.CartItem;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CartItemService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.MailService;
import com.hci.electric.services.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final AccountService accountService;
    private final Jwt jwt;
    private final Auth auth;
    private final MailService mailService;
    private final CartService cartService;
    private final CartItemService cartItemService;

    public AccountController(
        AccountService accountService,
        UserService userService,
        MailService mailService,
        CartService cartService,
        CartItemService cartItemService) {
        this.accountService = accountService;
        this.userService = userService;
        this.jwt = new Jwt();
        this.mailService = mailService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        LoginResponse response = new LoginResponse(false, null, null, null);
        User  user = this.userService.getByEmail(request.getEmail());
        
        if(user == null) {
            response.setMessage("User not found.");
            return ResponseEntity.status(404).body(response);
        }

        Account account = this.accountService.getByUserId(user.getId());
        if (account == null) {
            response.setMessage("Internal Error Server");
            return ResponseEntity.status(500).body(response);
        }
        
        if (this.accountService.checkPassword(request.getPassword(), account.getPassword()) == false){
            response.setMessage("Incorrect Password");
            return ResponseEntity.status(404).body(response);
        }

        String accessToken = this.jwt.generateToken(account.getId());

        String anonymouseId = null;
        // Cookie cookie = WebUtils.getCookie(httpServletRequest, "userId");
        
        

        // if (cookie != null) {
        //     anonymouseId = cookie.getValue();
        //     Cookie newCookie = new Cookie("userId", user.getId());
        //     newCookie.setMaxAge(180 * 24 * 60 * 60);
        //     newCookie.setPath("/");
        //     newCookie.setSecure(true);
        //     newCookie.setHttpOnly(true);
        //     newCookie.setDomain("hciuipro.com");
        //     if (anonymouseId != null) {
        //         transferBasket(anonymouseId, user.getId());
        //     }

        //     httpServletResponse.addCookie(newCookie);
        // }

        if (request.getUserCartId() != null) {
            
            transferBasket(request.getUserCartId(), user.getId());
        }

       
        response.setMessage("Login successfully");
        response.setAccessToken(accessToken);
        response.setUserId(account.getUserId());
        return ResponseEntity.status(200).body(response);

    }

    @PutMapping("/api")
    public ResponseEntity<Boolean> changePassword(HttpServletRequest httpServletRequest, @RequestBody ChangePasswordRequest request){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(400).body(false);
        }

        if (this.accountService.checkPassword(request.getOldPassword(), account.getPassword()) == false){
            return ResponseEntity.status(400).body(false);
        }

        account.setPassword(request.getNewPassword());
        if(this.accountService.edit(account) == null){
            return ResponseEntity.status(500).body(false);
        }
        return ResponseEntity.status(200).body(true);
    }

    @DeleteMapping("/api")
    public ResponseEntity<Boolean> deleteAccount(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(false);
        }
        account.setStatus(false);
        if (this.accountService.edit(account) == null){
            return ResponseEntity.status(500).body(false);
        }

        return ResponseEntity.status(200).body(true);
    }

    @GetMapping("/mail")
    public Boolean sendMail(){
        this.mailService.sendMail("nguyentri.alan@gmail.com");
        return true;
    }


    private void transferBasket(String anonymousId, String userId) {
        if (anonymousId.equals(userId)) {
            return;
        }

        Cart anonymousCart = this.cartService.getByUserId(anonymousId);
        if (anonymousCart == null) {
            return;
        }

        Cart userCart = this.cartService.getByUserId(userId);

        if (userCart == null) {
            anonymousCart.setUserId(userId);
            this.cartService.edit(anonymousCart);

            return;
        }

        List<CartItem> anonymouseCartItems = this.cartItemService.getAllItemsByCart(anonymousCart.getId());
        List<CartItem> userCartItems = this.cartItemService.getAllItemsByCart(userCart.getId());

        for (CartItem anonymousItem : anonymouseCartItems) {
            boolean isNewItem = true;
            
            for (CartItem userItem : userCartItems) {
                if (anonymousItem.getProductId().equals(userItem.getProductId())) {
                    userItem.setQuantity(userItem.getQuantity() + anonymousItem.getQuantity());
                    this.cartItemService.edit(userItem);
                    this.cartItemService.delete(anonymousItem);
                    isNewItem = false;

                    break;
                }
            }

            if (isNewItem) {
                anonymousItem.setCartId(userCart.getId());
                this.cartItemService.edit(anonymousItem);
            }
        }

    }
}
