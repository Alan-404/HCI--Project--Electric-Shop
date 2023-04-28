package com.hci.electric.dtos.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductResponse {
    private Boolean success = false;
    private String message = "";
    private ProductItem product = null;
}
