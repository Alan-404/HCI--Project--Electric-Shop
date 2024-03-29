package com.hci.electric.utils.queries;

public class BillQuery {
    public static final String queryGetBiilsByUser = "SELECT * FROM BILL WHERE USER_ID = ?1";
    public static final String queryGetBillsByUserAndStatus = "SELECT * FROM BILL WHERE USER_ID = ?1 AND STATUS =?2";
    public static final String paginateBillsByUser = "SELECT * FROM BILL WHERE USER_ID = ?1 LIMIT ?2 OFFSET ?3";

    public static final String paginateBillsByUserAndStatus = "SELECT * FROM BILL WHERE USER_ID = ?1 AND STATUS =?2 LIMIT ?3 OFFSET ?4";
    public static final String paginateBills = "SELECT * FROM BILL ORDER BY ORDER_TIME DESC LIMIT ?1 OFFSET ?2";
    public static final String findByPaymentTypeAndStatus =
        "SELECT * FROM BILL WHERE PAYMENT_TYPE =?1 AND STATUS =?2";
}
