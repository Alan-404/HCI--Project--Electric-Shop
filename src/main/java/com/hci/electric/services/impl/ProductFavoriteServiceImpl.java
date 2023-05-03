package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.List;

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
            item.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            return this.productFavoriteRepository.save(item);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ProductFavorite> getByUser(String userId) {
        try {
            return this.productFavoriteRepository.findByUser(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public int countByProduct(String productId) {
        try {
            return this.productFavoriteRepository.countByProductId(productId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(int id) {
        try {
            this.productFavoriteRepository.deleteById(id);
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
