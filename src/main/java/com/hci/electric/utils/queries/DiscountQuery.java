package com.hci.electric.utils.queries;

public class DiscountQuery {
    public static final String getByProductId = "SELECT * FROM DISCOUNT WHERE PRODUCT_ID = ?1";
}
