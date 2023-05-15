package com.hci.electric.dtos.warehouse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseResponse {
    private String productName;
    private String productId;
    private int quantity;
    private int id;
}
