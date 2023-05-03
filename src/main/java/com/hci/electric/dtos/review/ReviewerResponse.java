package com.hci.electric.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String avatar;
}
