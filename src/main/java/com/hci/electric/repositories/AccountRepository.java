package com.hci.electric.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Account;
import com.hci.electric.utils.queries.AccountQuery;

public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value = AccountQuery.queryByUserId, nativeQuery = true )
    Optional<Account> getByUserId(String userId);
}
