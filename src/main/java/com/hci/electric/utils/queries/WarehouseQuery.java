package com.hci.electric.utils.queries;

public class WarehouseQuery {
    public static final String getByProductId = "SELECT * FROM WAREHOUSE WHERE PRODUCT_ID = ?1";
    public static final String paginateWarehouse = "SELECT * FROM WAREHOUSE LIMIT ?1 OFFSET ?2";
}
