package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Banner;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BannerService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/banner")
public class BannerController {
    private final BannerService bannerService;
    private final AccountService accountService;

    private final Auth auth;

    public BannerController(BannerService bannerService, AccountService accountService){
        this.bannerService = bannerService;
        this.accountService = accountService;

        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<Banner> addBanner(@RequestBody Banner banner, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        
        Account account = this.auth.checkToken(token);
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString()) == false){
            return ResponseEntity.status(400).body(null);
        }

        Banner savedBanner = this.bannerService.save(banner);
        if (savedBanner == null){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.status(200).body(savedBanner);
    }

    @GetMapping("show")
    public ResponseEntity<List<Banner>> getAll(){
        List<Banner> banners = this.bannerService.getAll();

        if (banners == null){
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.status(200).body(banners);
    }
}
