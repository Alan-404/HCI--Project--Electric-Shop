package com.hci.electric.dtos.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Integer id;
    private double productPrice;
    private int quantity;
    private String productName;
    private String image;
}
