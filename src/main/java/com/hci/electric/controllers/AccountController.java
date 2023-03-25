package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.account.ChangePasswordRequest;
import com.hci.electric.dtos.account.LoginRequest;
import com.hci.electric.dtos.account.LoginResponse;
import com.hci.electric.middlewares.Auth;
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
    private final Auth auth;

    public AccountController(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
        this.jwt = new Jwt();

        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
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
}
