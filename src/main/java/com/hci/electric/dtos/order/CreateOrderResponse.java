package com.hci.electric.dtos.order;

import com.hci.electric.models.Bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse {
    private boolean success = false;
    private String message;
    private Bill bill;
}
