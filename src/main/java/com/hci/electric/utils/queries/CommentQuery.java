package com.hci.electric.utils.queries;

public class CommentQuery {
    public static final String queryGetCommentsOfProduct = "SELECT * FROM COMMENT WHERE PRODUCT_ID = ?1 AND REPLY IS NULL ORDER BY CREATED_AT";
    public static final String queryGetRepliesOfComment = "SELECT * FROM COMMENT WHERE REPLY = ?1 ORDER BY CREATED_AT";
    public static final String queryPaginateWithProduct = "SELECT * FROM COMMENT WHERE PRODUCT_ID = ?1 ORDER BY CREATED_AT OFFSET ?2 LIMIT ?3";
}
