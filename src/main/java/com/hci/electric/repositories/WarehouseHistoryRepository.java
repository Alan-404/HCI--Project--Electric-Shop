package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.utils.queries.WarehouseHistoryQuery;

public interface WarehouseHistoryRepository extends JpaRepository<WarehouseHistory, Integer> {
    @Query(value = WarehouseHistoryQuery.getByWarehouseId, nativeQuery = true)
    Optional<List<WarehouseHistory>> getByWarehouseId(String warehouseId);
}
