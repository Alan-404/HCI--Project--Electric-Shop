package com.hci.electric.dtos.productDetail;

import java.util.List;

import com.hci.electric.models.ProductDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    private String productId;
    private List<ProductDetail> products;
    private List<Integer> quantities;
    private List<Double> discounts;
}
