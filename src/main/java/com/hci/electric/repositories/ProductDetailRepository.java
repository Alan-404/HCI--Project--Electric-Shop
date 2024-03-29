package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.ProductDetail;
import com.hci.electric.utils.queries.ProductDetailQuery;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
    @Query(value = ProductDetailQuery.queryByProductId, nativeQuery = true)
    public Optional<List<ProductDetail>> getByProductId(String id);

    @Query(value = ProductDetailQuery.paginateProductDetail, nativeQuery = true)
    public Optional<List<ProductDetail>> paginateProductDetail(int limit, int offset);

    @Query(value = ProductDetailQuery.queryBestSeller, nativeQuery = true)
    public Optional<List<ProductDetail>> getBestSellers();
}
