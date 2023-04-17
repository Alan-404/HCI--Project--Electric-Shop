package com.hci.electric.dtos.user;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date birthDate;
    private String gender;
    private String address;
    private String avatar;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private boolean status;
    private String role;
}
