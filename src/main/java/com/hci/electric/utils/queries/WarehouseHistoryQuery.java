package com.hci.electric.utils.queries;

public class WarehouseHistoryQuery {
    public static final String getByWarehouseId = "SELECT * FROM WAREHOUSE_HISTORY WHERE WAREHOUSE_ID = ?1";
}
