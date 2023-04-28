package com.hci.electric.dtos.deliveryInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoRequest {
    private String userId;
    private String acceptorName;
    private String acceptorPhone;
    private String deliveryAddress;
}
