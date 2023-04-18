package com.hci.electric.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<ProductImage> getMediaByProduct(String productId){
        try{
            Optional<List<ProductImage>> media = this.productImageRepository.getMediaByProduct(productId);
            if (media.isPresent() == false){
                return new ArrayList<>();
            }

            return media.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteAllByProduct(String productId){
        try{
            List<ProductImage> media = this.getMediaByProduct(productId);
            if (media == null){
                return false;
            }

            this.productImageRepository.deleteAll(media);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveMedia(List<String> links){
        try{
            for (String link : links) {
                ProductImage item = new ProductImage();
                item.setLink(link);
                this.save(item);
            }

            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
