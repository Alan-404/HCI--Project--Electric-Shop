package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Product;
import com.hci.electric.utils.queries.ProductQuery;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = ProductQuery.queryPaginateProducts, nativeQuery = true)
    public Optional<List<Product>> paginate(int limit, int offset);

    @Query(value = ProductQuery.queryFindByDistributorId, nativeQuery = true)
    public List<Product> findByDistributorId(String distributorId);
}
