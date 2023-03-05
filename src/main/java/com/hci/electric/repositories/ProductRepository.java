package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    
}
