package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<WarehouseHistory> getByWarehouse(int warehouseId, boolean desc){
        try{
            Optional<List<WarehouseHistory>> histories;
            if (desc == false){
                histories = this.warehouseHistoryRepository.getByWarehouseId(warehouseId);
            }
            else{
                histories = this.warehouseHistoryRepository.getByWarehouseIdDesc(warehouseId);
            }
            if (histories.isPresent() == false){
                return new ArrayList<>();
            }
            return histories.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
