package com.hci.electric.dtos.product;

import com.hci.electric.models.Discount;
import com.hci.electric.models.Product;
import com.hci.electric.models.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    private Product product;
    private Discount discount;
    private Warehouse warehouse;
}
