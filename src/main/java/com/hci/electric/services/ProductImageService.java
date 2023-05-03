package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.ProductImage;

public interface ProductImageService {
    public ProductImage save(ProductImage image);
    public List<ProductImage> getMediaByProduct(String productId);
    public boolean deleteAllByProduct(String productId);
    public boolean saveMedia(List<String> links);
    public int countProductHaveImage();
}
