package com.hci.electric.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Discount;
import com.hci.electric.utils.queries.DiscountQuery;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    @Query(value = DiscountQuery.getByProductId, nativeQuery = true)
    Optional<Discount> getByProductId(String productId);
}
