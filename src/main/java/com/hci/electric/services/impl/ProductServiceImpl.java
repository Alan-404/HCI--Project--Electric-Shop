package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Product;
import com.hci.electric.repositories.ProductRepository;
import com.hci.electric.services.ProductService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Enums;
import com.hci.electric.utils.Libraries;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product){
        try{
            product.setId(Libraries.generateId(Constants.lengthId));
            product.setStatus(Enums.StatusProduct.BUSSINESS.toString());
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            product.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.productRepository.save(product);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Product getById(String id){
        try{
            Optional<Product> product = this.productRepository.findById(id);
            if (product.isPresent() == false){
                return null;
            }

            return product.get();
        }  
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
