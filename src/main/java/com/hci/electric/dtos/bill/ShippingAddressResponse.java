package com.hci.electric.dtos.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressResponse {
    private String acceptorName;
    private String acceptorPhone;
    private String deliveryAddress;
}
