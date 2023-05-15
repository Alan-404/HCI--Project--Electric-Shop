package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.ProductReview;
import com.hci.electric.repositories.ProductReviewRepository;
import com.hci.electric.services.ProductReviewService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;


@Service
public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;

    public ProductReviewServiceImpl(ProductReviewRepository productReviewRepository){
        this.productReviewRepository = productReviewRepository;
    }

    @Override
    public ProductReview save(ProductReview review){
        try{
            review.setId(Libraries.generateId(Constants.lengthId));
            review.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            return this.productReviewRepository.save(review);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }



    @Override
    public List<ProductReview> getByProduct(String productId){
        try{
            Optional<List<ProductReview>> review = this.productReviewRepository.getByProduct(productId);
            if (review.isPresent() == false){
                return null;
            }

            return review.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public int countRaingByValueAndProductId(int value, String productId) {
        try {
            int num = this.productReviewRepository.countRatingByValue(value, productId);

            return num;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
