package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    
}
