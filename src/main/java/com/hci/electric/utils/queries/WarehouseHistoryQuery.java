package com.hci.electric.utils.queries;

public class WarehouseHistoryQuery {
    public static final String getByWarehouseId = "SELECT * FROM WAREHOUSE_HISTORY WHERE WAREHOUSE_ID = ?1 ORDER BY CREATED_AT";
    public static final String getByWarehouseIdDesc = "SELECT * FROM WAREHOUSE_HISTORY WHERE WAREHOUSE_ID = ?1 ORDER BY CREATED_AT DESC";
}
