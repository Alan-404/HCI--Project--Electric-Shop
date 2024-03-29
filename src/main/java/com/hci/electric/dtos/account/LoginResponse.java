package com.hci.electric.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Boolean success = false;
    private String message = "";
    private String accessToken = "";
    private String userId = null;
}
