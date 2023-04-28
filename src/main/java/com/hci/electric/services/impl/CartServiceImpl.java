package com.hci.electric.services.impl;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Cart;
import com.hci.electric.repositories.CartRepository;
import com.hci.electric.services.CartService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;


@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart save(Cart cart){
        try{
            cart.setId(Libraries.generateId(Constants.lengthId));
            return this.cartRepository.save(cart);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Cart getByUserId(String userId){
        try{
            Optional<Cart> items = this.cartRepository.getByUserId(userId);
            if (items.isPresent() == false){
                return null;
            }

            return items.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Cart getById(String id){
        try{
            Optional<Cart> item = this.cartRepository.findById(id);
            if (item.isPresent() == false){
                return null;
            }

            return item.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Cart edit(Cart cart) {
        try {
            return this.cartRepository.save(cart);
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
    }
}
