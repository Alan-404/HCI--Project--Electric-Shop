package com.hci.electric.services;

import com.hci.electric.models.Warehouse;

public interface WarehouseService {
    public Warehouse save(Warehouse warehouse);
    public Warehouse getByProductId(String productId);
    public Warehouse edit(Warehouse warehouse);
}
