package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.OrderService;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;
    private final OrderService orderService;
    private final AccountService accountService;
    private final Jwt jwt;

    public BillController(BillService billService, OrderService orderService, AccountService accountService){
        this.billService = billService;
        this.orderService = orderService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable("id") String id, HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        if (token.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(null);
        }

        String accountId = this.jwt.extractAccountId(token.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(null);
        }

        Account account = this.accountService.getById(accountId);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        Bill bill = this.billService.getById(id);
        if (bill.getUserId().equals(account.getUserId()) == false){
            return ResponseEntity.status(403).body(null);
        } 

        return ResponseEntity.status(200).body(bill);
    }
}
