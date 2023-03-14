package com.hci.electric.dtos.discount;

import com.hci.electric.models.Discount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditDiscountResponse {
    private Boolean success;
    private String message;
    private Discount discount;
}
