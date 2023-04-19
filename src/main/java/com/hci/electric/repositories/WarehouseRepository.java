package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Warehouse;
import com.hci.electric.utils.queries.WarehouseQuery;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    @Query(value = WarehouseQuery.getByProductId, nativeQuery = true)
    Optional<Warehouse> getByProductId(String ProductId);

    @Query(value = WarehouseQuery.paginateWarehouse, nativeQuery = true)
    Optional<List<Warehouse>> paginateWarehouse(int limit, int offset);
}
