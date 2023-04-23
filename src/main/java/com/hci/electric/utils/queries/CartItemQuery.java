package com.hci.electric.utils.queries;

public class CartItemQuery {
    public static final String queryGetByCartIdAndProductId = "SELECT * FROM CART_ITEM WHERE CART_ID=?1 AND PRODUCT_ID = ?2";
    public static final String queryGetByCartId = "SELECT * FROM CART_ITEM WHERE CART_ID=?1";
    public static final String paginateGetByCartId = "SELECT * FROM CART_ITEM WHERE CART_ID = ?1 LIMIT ?2 OFFSET ?3";
    public static final String queryGetByCartIdAndStatus = "SELECT * FROM CART_ITEM WHERE CART_ID = ?1 AND STATUS = ?2";
}
