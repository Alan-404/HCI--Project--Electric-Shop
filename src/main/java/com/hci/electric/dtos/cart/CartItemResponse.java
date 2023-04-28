package com.hci.electric.dtos.cart;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private String productId;
    private String productName;
    private List<String> images;
    private double price;
    private double discount;
    private int quantity;
    private boolean status;
    private int warehouse;
}
