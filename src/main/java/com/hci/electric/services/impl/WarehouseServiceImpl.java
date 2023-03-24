package com.hci.electric.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Warehouse;
import com.hci.electric.repositories.WarehouseRepository;
import com.hci.electric.services.WarehouseService;

@Service
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

    @Override
    public Warehouse getByProductId(String productId){
        try{
            Optional<Warehouse> warehouse = this.warehouseRepository.getByProductId(productId);
            if (warehouse.isPresent() == false){
                return null;
            }

            return warehouse.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Warehouse edit(Warehouse warehouse){
        try{
            return this.warehouseRepository.save(warehouse);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
