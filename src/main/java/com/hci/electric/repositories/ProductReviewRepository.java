package com.hci.electric.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.ProductReview;
import com.hci.electric.utils.queries.ProductReviewQuery;

public interface ProductReviewRepository extends JpaRepository<ProductReview, String> {

    @Query(value = ProductReviewQuery.queryGetReviewProduct, nativeQuery = true)
    public Optional<List<ProductReview>> getByProduct(String productId);
}
