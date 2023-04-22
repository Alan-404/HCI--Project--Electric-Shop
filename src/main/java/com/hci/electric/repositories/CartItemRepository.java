package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.CartItem;
import com.hci.electric.utils.queries.CartItemQuery;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query(value = CartItemQuery.queryGetByCartIdAndProductId, nativeQuery = true)
    Optional<CartItem> getByUserIdAndProductId(String cartId, String productId);

    @Query(value = CartItemQuery.queryGetByCartId, nativeQuery = true)
    Optional<List<CartItem>> getByCartId(String cartId);
}
