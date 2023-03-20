package com.hci.electric.services.impl;

import com.hci.electric.repositories.ProductReviewRepository;
import com.hci.electric.services.ProductReviewService;

public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;

    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository){
        this.productReviewRepository = productReviewRepository;
    }
}
