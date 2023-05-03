package com.hci.electric.dtos.review;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private String id;
    private String userId;
    private String productId;
    private String content;
    private int stars;
    private Timestamp createdAt;
    private ReviewerResponse reviewer;
}
