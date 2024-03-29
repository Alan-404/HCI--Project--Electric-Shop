package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Cart;
import com.hci.electric.utils.queries.CartQuery;

public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = CartQuery.queryGetByUserId, nativeQuery = true)
    Optional<Cart> getByUserId(String userId);

    @Query(value = CartQuery.queryGetByUserIdAndProductId, nativeQuery = true)
    Optional<Cart> getByUserAndProduct(String userId, String productId);

    @Query(value = CartQuery.paginateGetByUserId, nativeQuery = true)
    Optional<List<Cart>> paginateGetByUserId(String userId, int limit, int offset);

    @Query(value = CartQuery.queryCartsByUserIdAndStatus, nativeQuery = true)
    Optional<List<Cart>> getByUserIdAndStatus (String userId, boolean status);
}
