package com.hci.electric.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.ProductCategory;
import com.hci.electric.utils.queries.ProductCategoryQuery;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    @Query(value = ProductCategoryQuery.queryByProductId, nativeQuery = true)
    public List<ProductCategory> findByProductId(String productId);
}
