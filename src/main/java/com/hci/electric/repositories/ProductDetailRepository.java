package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
    
}
