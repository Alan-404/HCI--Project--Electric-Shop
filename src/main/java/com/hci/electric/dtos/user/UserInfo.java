package com.hci.electric.dtos.user;

import com.hci.electric.models.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private User user;
    private String role;
}
