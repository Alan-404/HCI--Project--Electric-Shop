package com.hci.electric.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hci.electric.models.ProductCategory;
import com.hci.electric.repositories.ProductCategoryRepository;
import com.hci.electric.services.ProductCategoryService;


@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository){
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategory save(ProductCategory record){
        try{
            return this.productCategoryRepository.save(record);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean saveCategories(List<ProductCategory> records){
        try{
            boolean result = true;
            for (ProductCategory productCategory : records) {
                if (this.productCategoryRepository.save(productCategory) == null){
                    result = false;
                }
            }

            return result;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
