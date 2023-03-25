package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.discount.EditDiscountResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Discount;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DiscountService;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountService discountService;

    private final AccountService accountService;

    private Auth auth;

    public DiscountController(DiscountService discountService, AccountService accountService){
        this.discountService = discountService;
        this.accountService = accountService;

        this.auth = new Auth(this.accountService);
    }

    @PutMapping("/api")
    public ResponseEntity<EditDiscountResponse> editDiscount(@RequestBody Discount request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(accessToken);
        if (account == null){
            return ResponseEntity.status(403).body(new EditDiscountResponse(false, "Forbidden", null));
        }

        Discount discount = this.discountService.getByProductId(request.getProductId());
        if (discount == null){
            return ResponseEntity.status(404).body(new EditDiscountResponse(false, "Not Found Product", null));
        }

        Discount savedDiscount = this.discountService.save(discount);

        if (savedDiscount == null){
            return ResponseEntity.status(500).body(new EditDiscountResponse(false, "Internal Error Server", null));
        }

        return ResponseEntity.status(200).body(new EditDiscountResponse(true, "Saved Discount", savedDiscount));
    }
}
