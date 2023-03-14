package com.hci.electric.dtos.warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditQuantityWarehouseRequest {
    private String productId;
    private Integer quantity;
    private String type;
}
