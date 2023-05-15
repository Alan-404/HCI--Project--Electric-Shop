package com.hci.electric.dtos.productFavorite;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavoriteResponse {
    private int id;
    private String productId;
    private String productName;
    private String image;
    private Timestamp createdAt;
    private int totalHeart;
}
