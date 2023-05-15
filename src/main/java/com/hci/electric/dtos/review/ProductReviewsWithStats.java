package com.hci.electric.dtos.review;

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
public class ProductReviewsWithStats {
    private List<ProductReviewResponse> reviews = new ArrayList<>();
    private List<RatingStat> stats = new ArrayList<>();
}
