package com.hci.electric.dtos.product;

import java.util.List;

import com.hci.electric.models.Discount;
import com.hci.electric.models.Distributor;
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
public class ProductInfor {
    private Product product;
    private Distributor distributor;
    private List<ProductDetail> items;
    private List<Discount> discounts;
    private List<Warehouse> warehouses;
    private int reviews = 0;
    private double star = 0.0;
    private int purchases = 0;
}
