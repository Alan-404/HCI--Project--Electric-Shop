package com.hci.electric.services.impl;

import org.springframework.stereotype.Service;

import com.hci.electric.models.ProductDetail;
import com.hci.electric.repositories.ProductDetailRepository;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    private ProductDetailRepository productDetailRepository;
    
    public ProductDetailServiceImpl(ProductDetailRepository productDetailRepository){
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    public ProductDetail save(ProductDetail product){
        try{
            product.setId(Libraries.generateId(Constants.lengthId));
            return this.productDetailRepository.save(product);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
