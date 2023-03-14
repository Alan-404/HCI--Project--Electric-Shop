package com.hci.electric.services.impl;

import com.hci.electric.models.Warehouse;
import com.hci.electric.repositories.WarehouseRepository;
import com.hci.electric.services.WarehouseService;

public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository){
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public Warehouse save(Warehouse warehouse){
        try{
            return this.warehouseRepository.save(warehouse);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
