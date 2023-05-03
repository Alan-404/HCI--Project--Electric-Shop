package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.ProductFavorite;

public interface ProductFavoriteService {
    ProductFavorite save(ProductFavorite item);
    List<ProductFavorite> getByUser(String userId);
    int countByProduct(String productId);
    int delete(int id);
}
