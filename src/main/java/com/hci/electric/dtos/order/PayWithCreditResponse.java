package com.hci.electric.dtos.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayWithCreditResponse {
    private String message;
    private boolean success = false;
    private String url;
}
