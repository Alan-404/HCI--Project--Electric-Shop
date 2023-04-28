package com.hci.electric.dtos.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckItemRequest {
    private String cartId;
    private String productId;
    private boolean checked;
}
