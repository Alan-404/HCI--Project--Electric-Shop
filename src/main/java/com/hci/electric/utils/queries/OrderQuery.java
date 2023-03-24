package com.hci.electric.utils.queries;

public class OrderQuery {
    public static final String queryGetOrdersByBill = "SELECT * FROM USER_ORDER WHERE BILL_ID = ?1";   
}
