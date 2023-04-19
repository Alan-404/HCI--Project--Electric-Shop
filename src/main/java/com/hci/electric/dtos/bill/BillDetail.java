package com.hci.electric.dtos.bill;

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
public class BillDetail {
    private String id;
    private String userId;
    private Timestamp orderTime;
    private Double price;
    private String paymentType;
    private String status;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date birthDate;
    private String gender;
    private String address;
    private String avatar;
}
