package com.hci.electric.utils.queries;

public class BillQuery {
    public static final String queryGetBiilsByUser = "SELECT * FROM BILL WHERE USER_ID = ?1";
    public static final String queryGetBillsByUserAndStatus = "SELECT * FROM BILL WHERE USER_ID = ?1 AND STATUS =?2";
}
