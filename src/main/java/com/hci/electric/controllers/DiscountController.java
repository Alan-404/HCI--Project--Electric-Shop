package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.discount.EditDiscountResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Discount;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;
    private final Jwt jwt;
    private final AccountService accountService;

    public DiscountController(DiscountService discountService, AccountService accountService){
        this.discountService = discountService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    @PutMapping("/api")
    public ResponseEntity<EditDiscountResponse> editDiscount(@RequestBody Discount request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if (accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new EditDiscountResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new EditDiscountResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString().toLowerCase()) == false){
            return ResponseEntity.status(403).body(new EditDiscountResponse(false, "Forbidden", null));
        }

        Discount discount = this.discountService.getByProductId(request.getProductId());
        if (discount == null){
            return ResponseEntity.status(404).body(new EditDiscountResponse(false, "Not Found Product", null));
        }

        Discount savedDiscount = this.discountService.save(discount);

        return ResponseEntity.status(200).body(new EditDiscountResponse(true, "Saved Discount", savedDiscount));

    }
}
