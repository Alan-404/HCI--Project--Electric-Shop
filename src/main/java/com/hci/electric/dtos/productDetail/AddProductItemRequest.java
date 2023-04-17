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
    private int color;
    private String specifications;
    private Double price;
    private boolean status;
    private Double discount;
    private int quantity;
    private List<String> images;
}
