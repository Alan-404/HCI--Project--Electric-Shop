package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.WarehouseHistory;

public interface WarehouseHistoryRepository extends JpaRepository<WarehouseHistory, Integer> {
    
}
