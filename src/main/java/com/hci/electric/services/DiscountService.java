package com.hci.electric.services;

import com.hci.electric.models.Discount;

public interface DiscountService {
    public Discount save(Discount discount);
    public Discount getByProductId(String productId);
}
