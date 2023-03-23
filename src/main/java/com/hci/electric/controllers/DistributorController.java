package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.distributor.RegisterResponse;
import com.hci.electric.middlewares.Jwt;
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
    private final Jwt jwt;

    public DistributorController(DistributorService distributorService, AccountService accountService, UserService userService){
        this.distributorService = distributorService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    @PostMapping("/api")
    public ResponseEntity<RegisterResponse> register(@RequestBody Distributor distributor, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        String accountId = this.jwt.extractAccountId(token.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new RegisterResponse(false, "Invalid Token", null));
        }
        Account account = this.accountService.getById(accountId);
        if (account == null){
            return ResponseEntity.status(400).body(new RegisterResponse(false, "You must have registered User before registering as Distributor", null));
        }

        if (this.distributorService.getByUserId(account.getUserId()) != null){
            return ResponseEntity.status(400).body(new RegisterResponse(false, "You have already been a Distributor", null));
        }


        distributor.setUserId(account.getUserId());

        Distributor savedDistributor = this.distributorService.save(distributor);
        if (savedDistributor == null){
            return ResponseEntity.status(500).body(new RegisterResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new RegisterResponse(true, "You have already been a Distributor", savedDistributor));

    }
}   
