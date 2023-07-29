package com.hci.electric.utils.queries;

public class ProductQuery {
    public static final String queryPaginateProducts = "SELECT * FROM PRODUCT ORDER BY ID LIMIT ?1 OFFSET ?2";
    public static final String queryFindByDistributorId = "SELECT * FROM PRODUCT WHERE DISTRIBUTOR_ID = ?1";
}
