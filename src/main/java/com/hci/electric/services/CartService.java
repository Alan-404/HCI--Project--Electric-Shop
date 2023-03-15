package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Cart;



public interface CartService {
    public Cart save(Cart cart);
    public Cart edit(Cart cart);
    public List<Cart> getByUserId(String userId);
}
