package com.hci.electric.dtos.bill;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginateBill {
    private List<BillDetail> items;
    private int totalPages;
}
