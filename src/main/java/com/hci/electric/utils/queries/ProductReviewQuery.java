package com.hci.electric.utils.queries;

public class ProductReviewQuery {
    public static final String queryGetReviewProduct = "SELECT * FROM PRODUCT_REVIEW WHERE PRODUCT_ID = ?1 ORDER BY created_at DESC";
    public static final String queryCountRating = "SELECT count(*) FROM PRODUCT_REVIEW WHERE stars = ?1 AND product_id = ?2";
}
