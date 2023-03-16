package com.hci.electric.dtos.productCategory;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductCategoryRequest {
    private String productId;
    private List<String> categories;
}
