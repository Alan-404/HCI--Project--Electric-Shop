package com.hci.electric.dtos.product;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.hci.electric.models.Distributor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    private String id;
    private String name;
    private String description;
    private String information;
    private String distributorId;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private List<String> categories = new ArrayList<>();
    private Distributor distributor;
}
