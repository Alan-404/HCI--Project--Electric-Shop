package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

import com.hci.electric.dtos.user.EditUserRequest;
import com.hci.electric.dtos.user.RegisterRequest;
import com.hci.electric.dtos.user.RegisterResponse;
import com.hci.electric.dtos.user.UserInfo;
import com.hci.electric.dtos.user.UserRole;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Cart;
import com.hci.electric.models.CartItem;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CartItemService;
import com.hci.electric.services.CartService;
import com.hci.electric.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final CartItemService cartItemService;

    private final Auth auth;

    public UserController(
        UserService userService,
        AccountService accountService,
        CartService cartService,
        CartItemService cartItemService) {
        this.userService = userService;
        this.accountService = accountService;
        this.modelMapper = new ModelMapper();
        this.cartItemService = cartItemService;
        this.cartService = cartService;

        this.auth = new Auth(this.accountService);

    }

    @PostMapping("/api")
    public ResponseEntity<RegisterResponse> register(
        @RequestBody RegisterRequest request,
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) {
        String email = request.getEmail();
        if (this.userService.getByEmail(email) != null){
            return ResponseEntity.status(400).body(new RegisterResponse(false, "Email has been taken", null));
        }

        if (request.getPhone() != null) {
            if (this.userService.getByPhone(request.getPhone()) != null) {
                return ResponseEntity.status(400).body(new RegisterResponse(false, "Phone Number has been used", null));
            } 
        }

        User user = this.modelMapper.map(request, User.class);
        Account account = this.modelMapper.map(request, Account.class);

        User savedUser = this.userService.save(user);
        if(savedUser == null) {
            return ResponseEntity.status(500).body(new RegisterResponse(false, "Internal Error Server", null));
        }

        if (request.getRole() == null) {
            account.setRole("user");
        }

        account.setUserId(savedUser.getId());
        Account savedAccount = this.accountService.save(account);
        if(savedAccount == null) {
            return ResponseEntity.status(500).body(new RegisterResponse(false, "Internal Error Server", null));
        }

        // String anonymouseId = null;
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

        return ResponseEntity.status(200).body(new RegisterResponse(true, "Register User Successfully", savedUser));
    }

    @PutMapping("/api")
    public ResponseEntity<User> edit(HttpServletRequest httpServletRequest, @RequestBody EditUserRequest request){
        String accessToken = httpServletRequest.getHeader("Authorization");
        
        Account account = this.auth.checkToken(accessToken);
        if(account == null){
            return ResponseEntity.status(400).body(null);
        }
        User oldDataUser = this.userService.getById(account.getUserId());
        User user = this.modelMapper.map(request, User.class);
        user.setId(account.getUserId());
        user.setCreatedAt(oldDataUser.getCreatedAt());
        user.setEmail(oldDataUser.getEmail());

        if (request.getAvatar() == null) {
            user.setAvatar(oldDataUser.getAvatar());
        }

        User savedUser = this.userService.edit(user);
        if(savedUser == null){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.status(200).body(savedUser);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfo> getByToken(HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        
        Account account = this.auth.checkToken(accessToken);
        if(account == null){
            return ResponseEntity.status(400).body(null);
        }
        UserInfo infor = new UserInfo();
        User user = this.userService.getById(account.getUserId());
        
        if(user == null){
            return ResponseEntity.status(500).body(null);
        }
        infor.setUser(user);
        infor.setRole(account.getRole());
        return ResponseEntity.status(200).body(infor);

    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserRole>> getUsers(){
        List<User> users = this.userService.getUsers();
        if (users == null){
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
        List<UserRole> usersInfor = new ArrayList<>();
        for (User user : users) {
            UserRole item = this.modelMapper.map(user, UserRole.class);
            Account account = this.accountService.getByUserId(user.getId());
            item.setRole(account.getRole());
            item.setStatus(account.getStatus());

            usersInfor.add(item);
        }

        return ResponseEntity.status(200).body(usersInfor);
    }
}
