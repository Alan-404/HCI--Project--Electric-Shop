package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    
}
