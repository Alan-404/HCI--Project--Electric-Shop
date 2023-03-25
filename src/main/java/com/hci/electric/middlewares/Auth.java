package com.hci.electric.middlewares;

import com.hci.electric.models.Account;
import com.hci.electric.services.AccountService;

public class Auth {
    private final AccountService accountService;
    private final Jwt jwt;

    private final String preHeader = "Bearer ";
    private final String splitToken = " ";

    public Auth(AccountService accountService){
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    public Account checkToken(String header){
        if (header == null || header.startsWith(this.preHeader) == false){
            return null; 
        }

        String accountId = this.jwt.extractAccountId(header.split(this.splitToken)[1]); 
        if (accountId == null){
            return null;
        }

        Account account = this.accountService.getById(accountId);
        return account;
    }
}
