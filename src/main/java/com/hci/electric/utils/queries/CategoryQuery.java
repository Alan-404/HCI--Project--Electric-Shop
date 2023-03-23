package com.hci.electric.utils.queries;

public class CategoryQuery {
    public static final String paginate = "SELECT * FROM CATEGORY ORDER BY NAME LIMIT ?1 OFFSET ?2";
}
