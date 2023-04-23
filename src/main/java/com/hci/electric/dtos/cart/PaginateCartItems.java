package com.hci.electric.dtos.cart;

import java.util.List;

import com.hci.electric.models.CartItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginateCartItems {
    private List<CartItem> items;
    private int totalPages;
}
