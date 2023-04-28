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
public class RemoveMultiItemsRequest {
    private String cartId;
    private List<String> productIds;
}
