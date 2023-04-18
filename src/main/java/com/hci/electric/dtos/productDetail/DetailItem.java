package com.hci.electric.dtos.productDetail;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailItem {
    private String id;
    private String name;
    private String productId;
    private int color;
    private String specifications;
    private Double price;
    private boolean status;
    private Double discount;
    private int warehouse;
    private List<String> media;
    private Timestamp modifiedAt;
}
