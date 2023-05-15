package com.hci.electric.dtos.comment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String id;
    private CommentUser user;
    private String productId;
    private String content;
    private String reply;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    List<CommentResponse> replies = new ArrayList<>() ;
}
