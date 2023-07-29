package com.hci.electric.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResponse {
    private String id = null;
    private String message = "";
    private boolean status = false;
}
