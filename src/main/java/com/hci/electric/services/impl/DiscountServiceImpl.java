package com.hci.electric.services.impl;

import java.sql.Timestamp;

import com.hci.electric.models.Discount;
import com.hci.electric.repositories.DiscountRepository;
import com.hci.electric.services.DiscountService;

public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount save(Discount discount){
        try{
            discount.setModifiedAt(new Timestamp(System.currentTimeMillis()));

            return this.discountRepository.save(discount);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
