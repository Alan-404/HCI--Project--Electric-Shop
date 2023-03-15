package com.hci.electric.dtos.cart;

import com.hci.electric.models.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HandleCartResponse {
    private boolean success;
    private String message;
    private Cart cart;   
}
