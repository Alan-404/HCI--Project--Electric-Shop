package com.hci.electric.utils.queries;

public class UserQuery {
    public static final String queryByEmail = "SELECT * FROM USER_SYSTEM WHERE EMAIL = ?";
    public static final String queryGetByPhone = "SELECT * FROM USER_SYSTEM WHERE PHONE = ?";
}
