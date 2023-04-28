package com.hci.electric.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.distributor.RegisterResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Distributor;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DistributorService;
import com.hci.electric.services.UserService;

@RestController
@RequestMapping("/distributor")
public class DistributorController {
    private final DistributorService distributorService;
    private final AccountService accountService;

    private final Auth auth;

    public DistributorController(DistributorService distributorService, AccountService accountService, UserService userService){
        this.distributorService = distributorService;
        this.accountService = accountService;
        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<RegisterResponse> register(@RequestBody Distributor distributor, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(token);
        if (account == null) {
            return ResponseEntity.status(400).body(new RegisterResponse(false, "You must have registered User before registering as Distributor", null));
        }

        //distributor.setUserId(account.getUserId());

        Distributor savedDistributor = this.distributorService.save(distributor);
        if (savedDistributor == null) {
            return ResponseEntity.status(500).body(new RegisterResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new RegisterResponse(true, "Created Distributor", savedDistributor));

    }

    @GetMapping("/api")
    public List<Distributor> getAll() {

        List<Distributor> distributors = this.distributorService.getAll();

        return distributors;
    }
}   
