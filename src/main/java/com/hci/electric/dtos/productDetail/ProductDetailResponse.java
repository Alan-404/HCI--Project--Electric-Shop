package com.hci.electric.dtos.productDetail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private String id;
    private String objectID;
    private String name;
    private String brand;
    private List<String> categories;
    private Double price;
    private boolean status;
    private Double discount;
    private String image;
    private String specifications;
    private String color;
    private String model;
}
