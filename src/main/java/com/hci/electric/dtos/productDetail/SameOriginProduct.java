package com.hci.electric.dtos.productDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SameOriginProduct {
    private String id;
    private double realPrice;
    private String specifications;
    private int color;
    private String image;
}
