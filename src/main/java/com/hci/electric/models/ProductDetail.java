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
@Table(name = "PRODUCT_DETAIL")
public class ProductDetail {
    @Id
    private String id;
    private String productId;
    private int color;
    private String specifications;
    private Double price;
    private boolean status;
    private Timestamp modifiedAt;
}
