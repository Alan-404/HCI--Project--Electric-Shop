package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    
}
