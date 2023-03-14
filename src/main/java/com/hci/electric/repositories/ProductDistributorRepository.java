package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.ProductDistributor;

public interface ProductDistributorRepository extends JpaRepository<ProductDistributor, Integer> {
    
}
