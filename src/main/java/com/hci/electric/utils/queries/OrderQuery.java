package com.hci.electric.utils.queries;

public class OrderQuery {
    public static final String queryGetById = "SELECT * FROM USER_ORDER WHERE ID = ?1";
    public static final String queryGetOrdersByBill = "SELECT * FROM USER_ORDER WHERE BILL_ID = ?1";
    public static final String queryGetOrderByBillAndReviewStatus = "SELECT * FROM USER_ORDER WHERE BILL_ID = ?1 AND REVIEWED = ?2";
    public static final String queryGetOrdersByProductId = "SELECT * FROM USER_ORDER WHERE PRODUCT_ID = ?1";
}
