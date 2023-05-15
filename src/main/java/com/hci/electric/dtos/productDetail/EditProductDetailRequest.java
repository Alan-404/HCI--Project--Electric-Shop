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
public class EditProductDetailRequest {
    private String id;
    private String productId;
    private Double discount;
    private int quantity;
    private String specifications;
    private List<String> images;
    private int color;
    private Double price;
    private boolean status;
    private boolean showOnHomePage;
}
