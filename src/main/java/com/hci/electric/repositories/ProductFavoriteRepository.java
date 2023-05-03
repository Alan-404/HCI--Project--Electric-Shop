package com.hci.electric.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.ProductFavorite;
import com.hci.electric.utils.queries.ProductFavoriteQuery;

public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, Integer> {
    @Query(value = ProductFavoriteQuery.countByProductId, nativeQuery = true)
    public int countByProductId(String productId);

    @Query(value = ProductFavoriteQuery.queryFindByUser, nativeQuery = true)
    public List<ProductFavorite> findByUser(String userId);
}
