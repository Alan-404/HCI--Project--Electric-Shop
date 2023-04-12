package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.ProductFavorite;

public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, Integer> {
    
}
