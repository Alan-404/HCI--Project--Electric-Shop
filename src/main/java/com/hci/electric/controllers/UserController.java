package com.hci.electric.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.user.RegisterRequest;
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

    public UserController(UserService userService, AccountService accountService){
        this.userService = userService;
        this.accountService = accountService;
        this.modelMapper = new ModelMapper();
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
}
