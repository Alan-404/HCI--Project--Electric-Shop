package com.hci.electric.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.User;
import com.hci.electric.utils.queries.UserQuery;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = UserQuery.queryByEmail, nativeQuery = true)
    Optional<User> getUserByEmail(String email);
}
