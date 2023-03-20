package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {
    
}
