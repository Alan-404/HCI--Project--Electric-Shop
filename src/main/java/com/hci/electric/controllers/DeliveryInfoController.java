package com.hci.electric.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.deliveryInfo.DeliveryInfoRequest;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.DeliveryInfo;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DeliveryInfoService;

@RestController
@RequestMapping("/delivery")
public class DeliveryInfoController {
    private final DeliveryInfoService deliveryInfoService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final Auth auth;

    public DeliveryInfoController(DeliveryInfoService deliveryInfoService, AccountService accountService) {
        this.deliveryInfoService = deliveryInfoService;
        this.accountService = accountService;
        this.auth = new Auth(this.accountService);
        this.modelMapper = new ModelMapper();
    }

    
    @GetMapping("/my")
    public ResponseEntity<List<DeliveryInfo>> getMyDeliveryInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            return ResponseEntity.status(400).body(null);
        }

        List<DeliveryInfo> deliveryInfos = this.deliveryInfoService.getByUserId(account.getUserId());

        return ResponseEntity.status(200).body(deliveryInfos);
    }

    @PostMapping("/add")
    public ResponseEntity<DeliveryInfo> addDeliveryInfo(
        @RequestBody DeliveryInfoRequest request,
        HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            return ResponseEntity.status(400).body(null);
        }

        DeliveryInfo deliveryInfo = this.modelMapper.map(request, DeliveryInfo.class);
        
        deliveryInfo.setUserId(account.getUserId());
        this.deliveryInfoService.save(deliveryInfo);

        return ResponseEntity.status(200).body(deliveryInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(
        @PathVariable("id") Integer id,
        HttpServletRequest httpServletRequest) {
        
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null) {
            return ResponseEntity.status(400).body(null);
        }

        this.deliveryInfoService.delete(id);

        return ResponseEntity.status(200).body(id);
    }
}
