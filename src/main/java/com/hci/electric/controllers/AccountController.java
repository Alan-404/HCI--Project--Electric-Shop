package com.hci.electric.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.account.LoginRequest;
import com.hci.electric.dtos.account.LoginResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.UserService;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;
    private final AccountService accountService;
    private final Jwt jwt;

    public AccountController(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
        this.jwt = new Jwt();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        User  user = this.userService.getByEmail(request.getEmail());
        if(user == null){
            return ResponseEntity.status(404).body(new LoginResponse(false, "Not found user", ""));
        }

        Account account = this.accountService.getByUserId(user.getId());
        if(account == null){
            return ResponseEntity.status(500).body(new LoginResponse(false, "Internal Error Server", ""));
        }
        
        if(this.accountService.checkPassword(request.getPassword(), account.getPassword()) == false){
            return ResponseEntity.status(404).body(new LoginResponse(false, "Incorrect Password", ""));
        }
        String accessToken = this.jwt.generateToken(account.getId());
        return ResponseEntity.status(200).body(new LoginResponse(true, "Login Successfully", accessToken));

    }
}
