package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hci.electric.models.Account;
import com.hci.electric.repositories.AccountRepository;
import com.hci.electric.services.AccountService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;


@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Account save(Account account){
        try{
            account.setId(Libraries.generateId(Constants.lengthId));
            account.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            account.setStatus(true);
            account.setPassword(this.bCryptPasswordEncoder.encode(account.getPassword()));
            return this.accountRepository.save(account);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Account getById(String id){
        try{
            Optional<Account> account = this.accountRepository.findById(id);
            if(account.isPresent() == false)
                return null;
            return account.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Account getByUserId(String userId){
        try{
            Optional<Account> account = this.accountRepository.getByUserId(userId);
            if (account.isPresent() == false)
                return null;

            return account.get();
        }   
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean checkPassword(String rawPassword, String encodedPassword){
        try{
            return this.bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
