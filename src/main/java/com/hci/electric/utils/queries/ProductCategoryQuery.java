package com.hci.electric.utils.queries;

public class ProductCategoryQuery {
    public static final String queryByProductId = "SELECT * FROM product_category WHERE PRODUCT_ID = ?1";
    
    public static final String queryByCategoryId = "SELECT * FROM product_category WHERE category_id = ?1";
}
