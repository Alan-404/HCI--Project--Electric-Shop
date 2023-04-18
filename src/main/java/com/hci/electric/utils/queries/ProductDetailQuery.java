package com.hci.electric.utils.queries;

public class ProductDetailQuery {
    public static final String queryByProductId = "SELECT * FROM PRODUCT_DETAIL WHERE PRODUCT_ID = ?1";  
    public static final String paginateProductDetail = "SELECT * FROM PRODUCT_DETAIL LIMIT ?1 OFFSET ?2"; 
}
