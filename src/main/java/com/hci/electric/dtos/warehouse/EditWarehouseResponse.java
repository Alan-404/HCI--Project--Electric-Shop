package com.hci.electric.dtos.warehouse;

import com.hci.electric.models.Warehouse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditWarehouseResponse {
    private Boolean success;
    private String message;
    private Warehouse warehouse;
}
