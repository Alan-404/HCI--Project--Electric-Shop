package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Warehouse;

public interface WarehouseService {
    public Warehouse save(Warehouse warehouse);
    public Warehouse getByProductId(String productId);
    public Warehouse edit(Warehouse warehouse);
    public List<Warehouse> getAll();
    public List<Warehouse> paginateWarehouse(int page, int num);
    
}
