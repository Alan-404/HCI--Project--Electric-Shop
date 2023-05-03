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
    private String distributorId;
    private String description;
    private String information;
    private int warehouse;
    private List<String> media;
    private List<SameOriginProduct> sameOriginProducts;
    private Timestamp modifiedAt;
    private float averageRating;
    private int numReviews;
    private boolean canReview = false;
}
