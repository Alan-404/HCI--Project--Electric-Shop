package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.List;
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
    public List<Cart> getByUserId(String userId){
        try{
            Optional<List<Cart>> items = this.cartRepository.getByUserId(userId);
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

    /* @Override
    public Cart save(Cart cart){
        try{
            cart.setId(Libraries.generateId(Constants.lengthId));
            cart.setStatus(false);
            cart.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            cart.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.cartRepository.save(cart);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Cart edit(Cart cart){
        try{
            cart.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.cartRepository.save(cart);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Cart> getByUserId(String userId){
        try{
            Optional<List<Cart>> items = this.cartRepository.getByUserId(userId);
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
    public Cart getByUserAndProduct(String userId, String productId){
        try{
            Optional<Cart> item = this.cartRepository.getByUserAndProduct(userId, productId);
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
    public List<Cart> paginateGetByUserId(String userId, int limit, int offset){
        try{
            Optional<List<Cart>> lstItems = this.cartRepository.paginateGetByUserId(userId, limit, offset);
            if (lstItems.isPresent() == false){
                return null;
            }

            return lstItems.get();
        }  
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(Cart cart){
        try{
            this.cartRepository.delete(cart);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStatusCarts(List<Cart> items, boolean status){
        try{
            for (Cart cart : items) {
                cart.setStatus(status);
            }

            this.cartRepository.saveAll(items);

            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cart> getByUserIdAndStatus(String userId, boolean status){
        try{
            Optional<List<Cart>> items = this.cartRepository.getByUserIdAndStatus(userId, status);
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
    public boolean deleteCarts(List<Cart> items){
        try{
            this.cartRepository.deleteAll(items);

            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    } */
}
