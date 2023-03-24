package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Discount;
import com.hci.electric.repositories.DiscountRepository;
import com.hci.electric.services.DiscountService;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository){
        this.discountRepository = discountRepository;
    }

    @Override
    public Discount save(Discount discount){
        try{
            discount.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            discount.setStatus(true);
            return this.discountRepository.save(discount);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Discount getByProductId(String productId){
        try{
            Optional<Discount> discount = this.discountRepository.getByProductId(productId);
            if (discount.isPresent() == false)
                return null;
            
            return discount.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
