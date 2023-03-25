package com.hci.electric.dtos.bill;

import java.util.List;

import com.hci.electric.dtos.productDetail.ProductItem;
import com.hci.electric.models.Bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillInfo {
    private Bill bill;
    private List<ProductItem> items;
}
