package com.hci.electric.utils.queries;

public class ProductFavoriteQuery {
    public static final String countByProductId = "SELECT COUNT(*) FROM PRODUCT_FAVORITE WHERE PRODUCT_ID = ?1";
    public static final String queryFindByUser = "SELECT * FROM PRODUCT_FAVORITE WHERE USER_ID = ?1";
}
