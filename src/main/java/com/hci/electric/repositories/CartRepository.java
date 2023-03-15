package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Cart;
import com.hci.electric.utils.queries.CartQuery;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = CartQuery.queryGetByUserId, nativeQuery = true)
    Optional<List<Cart>> getByUserId(String userId);
}
