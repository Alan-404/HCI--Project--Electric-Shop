package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.ProductCategory;

public interface ProductCategoryService {
    public ProductCategory save(ProductCategory record);
    public boolean saveCategories(List<ProductCategory> records);
}
