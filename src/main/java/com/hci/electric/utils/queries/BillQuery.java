package com.hci.electric.utils.queries;

public class BillQuery {
    public static final String queryGetBiilsByUser = "SELECT * FROM BILL WHERE USER_ID = ?1";
}
