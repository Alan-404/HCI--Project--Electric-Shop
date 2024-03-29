package com.hci.electric.dtos.productDetail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductItemRequest {
    private String productId;
    private List<Integer> colors;
    private String specifications;
    private Double price;
    private boolean status;
}
