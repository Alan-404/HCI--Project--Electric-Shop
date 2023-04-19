package com.hci.electric.services.impl;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<Warehouse> getAll(){
        try{
            return this.warehouseRepository.findAll();
        }  
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Warehouse> paginateWarehouse(int page, int num){
        try{
            Optional<List<Warehouse>> warehouses = this.warehouseRepository.paginateWarehouse(num, (page-1)*num);
            if (warehouses.isPresent() == false){
                return new ArrayList<>();
            }

            return warehouses.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
