package com.hci.electric.dtos.productCategory;

import java.util.List;

import com.hci.electric.models.Category;
import com.hci.electric.models.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductCategoryResponse {
    private boolean success;
    private String message;
    private Product product;
    private List<Category> categories;
}
