package com.hci.electric.services;

import com.hci.electric.models.Account;

public interface AccountService {
    public Account save(Account account);
    public Account edit(Account account);
    public Account getById(String id);
    public Account getByUserId(String userId);
    public Boolean checkPassword(String rawPassword, String encodedPassword);
}
