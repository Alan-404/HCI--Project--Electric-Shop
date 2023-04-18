package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.ProductImage;
import com.hci.electric.utils.queries.ProductImageQuery;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query(value = ProductImageQuery.getMediaByProduct, nativeQuery = true)
    Optional<List<ProductImage>> getMediaByProduct(String productId);
}
