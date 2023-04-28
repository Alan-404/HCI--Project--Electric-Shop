package com.hci.electric.dtos.bill;


import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private String id;
    private String userId;
    private Timestamp orderDate;
    private double price;
    private String paymentType;
    private String status;
    private List<OrderItem> orderItems;
}
