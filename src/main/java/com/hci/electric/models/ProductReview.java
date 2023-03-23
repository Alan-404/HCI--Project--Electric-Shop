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
@Table(name = "PRODUCT_REVIEW")
public class ProductReview {
    @Id
    private String id;
    private String orderId;
    private String content;
    private int stars;
    private Timestamp createdAt;
}
