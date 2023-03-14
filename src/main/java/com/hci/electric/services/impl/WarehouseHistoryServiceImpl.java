package com.hci.electric.services.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.repositories.WarehouseHistoryRepository;
import com.hci.electric.services.WarehouseHistoryService;

@Service
public class WarehouseHistoryServiceImpl implements WarehouseHistoryService {
    private WarehouseHistoryRepository warehouseHistoryRepository;

    public WarehouseHistoryServiceImpl(WarehouseHistoryRepository warehouseHistoryRepository){
        this.warehouseHistoryRepository = warehouseHistoryRepository;
    }

    @Override
    public WarehouseHistory save(WarehouseHistory record){
        try{
            record.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            return this.warehouseHistoryRepository.save(record);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
