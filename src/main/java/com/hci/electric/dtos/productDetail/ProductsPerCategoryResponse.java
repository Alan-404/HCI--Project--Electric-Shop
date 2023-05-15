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
public class ProductsPerCategoryResponse {
    private String id;
    private List<DetailItem> products;
}
