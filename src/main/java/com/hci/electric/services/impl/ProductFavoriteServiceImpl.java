package com.hci.electric.services.impl;

import org.springframework.stereotype.Service;

import com.hci.electric.models.ProductFavorite;
import com.hci.electric.repositories.ProductFavoriteRepository;
import com.hci.electric.services.ProductFavoriteService;

@Service
public class ProductFavoriteServiceImpl implements ProductFavoriteService {
    private ProductFavoriteRepository productFavoriteRepository;

    public ProductFavoriteServiceImpl(ProductFavoriteRepository productFavoriteRepository){
        this.productFavoriteRepository = productFavoriteRepository;
    }

    @Override
    public ProductFavorite save(ProductFavorite item){
        try{
            return this.productFavoriteRepository.save(item);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

}
