package com.hci.electric.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DISTRIBUTOR")
public class Distributor {
    @Id
    private String id;
    private String name;
    private String phone;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
