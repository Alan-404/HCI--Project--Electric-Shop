package com.hci.electric.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String information;
    private String distributorId;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
}
