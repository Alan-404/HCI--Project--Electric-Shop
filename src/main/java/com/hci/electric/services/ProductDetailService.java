package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.ProductDetail;

public interface ProductDetailService {
    public ProductDetail save(ProductDetail product);
    public List<ProductDetail> getByProductId(String id);
    public ProductDetail getById(String id);
}
