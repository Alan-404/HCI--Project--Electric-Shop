package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.ProductCategory;

public interface ProductCategoryService {
    public ProductCategory save(ProductCategory record);
    public boolean saveCategories(List<ProductCategory> records);
    public List<ProductCategory> getByProductId(String productId);
    public List<ProductCategory> getByCategoryId(String categoryId);
    public void delete(int productCategoryId);
}
