package com.hci.electric.dtos.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditBillResponse {
    private BillResponse bill;
    private String message;
    private boolean success = false;
}
