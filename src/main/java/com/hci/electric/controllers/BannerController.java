package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Banner;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BannerService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/banner")
public class BannerController {
    private final BannerService bannerService;
    private final Jwt jwt;
    private final AccountService accountService;
    public BannerController(BannerService bannerService, AccountService accountService){
        this.bannerService = bannerService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    @PostMapping("/api")
    public ResponseEntity<Banner> addBanner(@RequestBody Banner banner, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(null);
        }

        String accountId = this.jwt.extractAccountId(token.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(null);
        }
        Account account = this.accountService.getById(accountId);
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString()) == false){
            return ResponseEntity.status(400).body(null);
        }

        Banner savedBanner = this.bannerService.save(banner);
        if (savedBanner == null){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.status(200).body(savedBanner);
    }
}
