package com.hci.electric.services;


import com.hci.electric.models.Cart;



public interface CartService {
    public Cart save(Cart cart);
    public Cart getById(String id);
    public Cart getByUserId(String userId);
    public Cart edit(Cart cart);
}
