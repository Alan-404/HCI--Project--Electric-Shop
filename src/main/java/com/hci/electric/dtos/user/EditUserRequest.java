package com.hci.electric.dtos.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private Date birthDate;
    private String gender;
    private String address;
}
