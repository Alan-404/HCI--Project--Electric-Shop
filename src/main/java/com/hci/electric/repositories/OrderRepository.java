package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
    
}
