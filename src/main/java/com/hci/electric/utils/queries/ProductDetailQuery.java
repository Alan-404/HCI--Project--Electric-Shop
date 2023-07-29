package com.hci.electric.utils.queries;

public class ProductDetailQuery {
    public static final String queryByProductId = "SELECT * FROM PRODUCT_DETAIL WHERE PRODUCT_ID = ?1 AND STATUS = true";  
    public static final String paginateProductDetail = "SELECT * FROM PRODUCT_DETAIL LIMIT ?1 OFFSET ?2";
    public static final String queryBestSeller = "SELECT * FROM PRODUCT_DETAIL ORDER BY TOTAL_SALES DESC";
}
