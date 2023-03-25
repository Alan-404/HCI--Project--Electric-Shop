package com.hci.electric.dtos.productDetail;

import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    private Product product;
    private ProductDetail detail;
}
