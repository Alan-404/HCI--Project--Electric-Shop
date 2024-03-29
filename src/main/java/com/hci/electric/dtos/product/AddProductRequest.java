package com.hci.electric.dtos.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductRequest {
    private String name;
    private String description;
    private String information;
    private String distributorId;
    private List<String> categories;
}
