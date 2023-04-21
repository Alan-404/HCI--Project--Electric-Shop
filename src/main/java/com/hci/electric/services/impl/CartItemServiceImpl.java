package com.hci.electric.services.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.hci.electric.models.CartItem;
import com.hci.electric.repositories.CartItemRepository;
import com.hci.electric.services.CartItemService;


@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository){
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem save(CartItem item){
        try{
            item.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            item.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.cartItemRepository.save(item);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
