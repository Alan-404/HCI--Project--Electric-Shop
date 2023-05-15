package com.hci.electric.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewRequest {
    private Integer orderId;
    private String content;
    private Integer stars;
    private String productId;
}
