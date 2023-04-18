package com.hci.electric.utils.queries;

public class ProductImageQuery {
    public static final String getMediaByProduct = "SELECT * FROM PRODUCT_IMAGE WHERE PRODUCT_ID=?1";
}
