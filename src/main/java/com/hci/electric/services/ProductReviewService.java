package com.hci.electric.services;



import java.util.List;

import com.hci.electric.models.ProductReview;

public interface ProductReviewService {
    public ProductReview save(ProductReview review);
    public List<ProductReview> getByProduct(String productId);
    public int countRaingByValueAndProductId(int value, String productId);
    public ProductReview getById(String id);
    public ProductReview edit(ProductReview review);
}
