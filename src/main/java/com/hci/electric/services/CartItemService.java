package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.CartItem;

public interface CartItemService {
    public CartItem save(CartItem item);
    public CartItem edit(CartItem item);
    public CartItem getByCartIdAndProduct(String userId, String productId);
    public Boolean changeStatusInCart(String cartId, boolean status);
    public List<CartItem> getAllItemsByCart(String cartId);
}
