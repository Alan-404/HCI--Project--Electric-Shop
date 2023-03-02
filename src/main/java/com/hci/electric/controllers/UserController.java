package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.user.EditUserRequest;
import com.hci.electric.dtos.user.RegisterRequest;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final Jwt jwt;

    public UserController(UserService userService, AccountService accountService){
        this.userService = userService;
        this.accountService = accountService;
        this.modelMapper = new ModelMapper();
        this.jwt = new Jwt();

    }

    @PostMapping("/api")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request){
        User user = this.modelMapper.map(request, User.class);
        Account account = this.modelMapper.map(request, Account.class);

        User savedUser = this.userService.save(user);
        if(savedUser == null){
            return ResponseEntity.status(500).body(null);
        }

        account.setUserId(savedUser.getId());
        Account savedAccount = this.accountService.save(account);
        if(savedAccount == null){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.status(200).body(savedUser);
    }

    @PutMapping("/api")
    public ResponseEntity<User> edit(HttpServletRequest httpServletRequest, @RequestBody EditUserRequest request){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if(accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(null);
        }

        String accountId = this.jwt.extractUserId(accessToken.split(" ")[1]);
        if(accountId == null){
            return ResponseEntity.status(400).body(null);
        }

        Account account = this.accountService.getById(accountId);
        if(account == null){
            return ResponseEntity.status(400).body(null);
        }
        User oldDataUser = this.userService.getById(account.getUserId());
        User user = this.modelMapper.map(request, User.class);
        user.setId(account.getUserId());
        user.setCreatedAt(oldDataUser.getCreatedAt());
        user.setEmail(oldDataUser.getEmail());
        User savedUser = this.userService.edit(user);
        if(savedUser == null){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.status(200).body(savedUser);

    }

    @GetMapping("/info")
    public ResponseEntity<User> getByToken(HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if(accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(null);
        }
        String accountId = this.jwt.extractUserId(accessToken.split(" ")[1]);
        if(accountId == null){
            return ResponseEntity.status(400).body(null);
        }

        Account account = this.accountService.getById(accountId);
        if(account == null){
            return ResponseEntity.status(400).body(null);
        }
        User user = this.userService.getById(account.getUserId());
        return ResponseEntity.status(200).body(user);

    }
}
