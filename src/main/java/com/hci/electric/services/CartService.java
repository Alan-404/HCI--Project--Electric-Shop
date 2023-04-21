package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Cart;



public interface CartService {
    public Cart save(Cart cart);
    public Cart getById(String id);
    public List<Cart> getByUserId(String userId);
    /* public Cart save(Cart cart);
    public Cart edit(Cart cart);
    public List<Cart> getByUserId(String userId);
    public Cart getByUserAndProduct(String userId, String productId);
    public Cart getById(String id);
    public List<Cart> paginateGetByUserId(String userId, int limit, int offset);
    public boolean delete(Cart cart);
    public boolean updateStatusCarts(List<Cart> items, boolean status);
    public List<Cart> getByUserIdAndStatus(String userId, boolean status);
    public boolean deleteCarts(List<Cart> items); */
}
