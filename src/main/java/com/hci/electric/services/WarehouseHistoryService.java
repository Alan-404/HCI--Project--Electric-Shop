package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.WarehouseHistory;

public interface WarehouseHistoryService {
    public WarehouseHistory save(WarehouseHistory record);
    public List<WarehouseHistory> getByWarehouse(int warehouseId, boolean desc);
}
