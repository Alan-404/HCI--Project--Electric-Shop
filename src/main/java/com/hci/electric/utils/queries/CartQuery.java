package com.hci.electric.utils.queries;

public class CartQuery {
    public static final String queryGetByUserId = "SELECT * FROM CART WHERE USER_ID = ?1 ORDER BY ID";
    public static final String queryGetByUserIdAndProductId = "SELECT * FROM CART WHERE USER_ID=?1 AND PRODUCT_ID = ?2";
    public static final String paginateGetByUserId = "SELECT * FROM CART WHERE USER_ID = ?1 ORDER BY ID LIMIT ?2 OFFSET ?3";
}
