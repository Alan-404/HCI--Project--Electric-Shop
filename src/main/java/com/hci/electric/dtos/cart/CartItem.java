package com.hci.electric.dtos.cart;

import com.hci.electric.models.Cart;
import com.hci.electric.models.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Cart cart;
    private Product product;
}
