package com.hci.electric.dtos.product;

import java.util.List;

import com.hci.electric.models.Discount;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInformation {
    private Product product;
    private List<ProductDetail> details;
    private List<Discount> discounts;
    private List<Warehouse> warehouses;
}
