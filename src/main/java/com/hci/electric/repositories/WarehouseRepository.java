package com.hci.electric.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hci.electric.models.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    
}
