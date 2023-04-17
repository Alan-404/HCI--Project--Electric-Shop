package com.hci.electric.services.impl;

import org.springframework.stereotype.Service;

import com.hci.electric.models.ProductImage;
import com.hci.electric.repositories.ProductImageRepository;
import com.hci.electric.services.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {
    private ProductImageRepository productImageRepository;
    public ProductImageServiceImpl(ProductImageRepository productImageRepository){
        this.productImageRepository = productImageRepository;
    }
    @Override
    public ProductImage save(ProductImage image){
        try{
            image.setMain(true);
            return this.productImageRepository.save(image);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
