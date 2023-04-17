package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    
}
