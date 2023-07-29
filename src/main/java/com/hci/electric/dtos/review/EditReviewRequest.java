package com.hci.electric.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditReviewRequest {
    private String id;
    private int stars;
    private String content;
}
