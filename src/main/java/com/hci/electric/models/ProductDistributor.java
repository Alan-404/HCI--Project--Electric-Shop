package com.hci.electric.models;

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
@Table(name = "PRODUCT_DISTRIBUTOR")
public class ProductDistributor {
    @Id
    private Integer id;
    private String productId;
    private String distributorId;
}
