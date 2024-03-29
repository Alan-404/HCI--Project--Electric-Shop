package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public CartItem edit(CartItem cart){
        try{
            cart.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            return this.cartItemRepository.save(cart);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public CartItem getByCartIdAndProduct(String userId, String productId){
        try{
            Optional<CartItem> item = this.cartItemRepository.getByUserIdAndProductId(userId, productId);
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
    public List<CartItem> getAllItemsByCart(String cartId){
        try{
            Optional<List<CartItem>> items = this.cartItemRepository.getByCartId(cartId);
            if (items.isPresent() == false){
                return new ArrayList<>();
            }

            return items.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean changeStatusInCart(String cartId, boolean status){
        try{
            List<CartItem> items = this.getAllItemsByCart(cartId);
            if (items == null){
                return null;
            }

            for (CartItem cartItem : items) {
                cartItem.setStatus(status);
            }

            this.cartItemRepository.saveAll(items);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CartItem> paginateByCartId(String cartId, int page, int num){
        try{
            Optional<List<CartItem>> items = this.cartItemRepository.paginateByCartId(cartId, num, (page-1)*num);
            if (items.isPresent() == false){
                return new ArrayList<>();
            }

            return items.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CartItem> getByCartAndStatus(String cartId, boolean status){
        try{
            Optional<List<CartItem>> items = this.cartItemRepository.getByCartAndStatus(cartId, status);
            if (items.isPresent() == false){
                return new ArrayList<>();
            }

            return items.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(CartItem item){
        try{
            this.cartItemRepository.delete(item);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAllItemsForOrder(String cartId){
        try{
            List<CartItem> items = this.getByCartAndStatus(cartId, true);
            if (items == null || items.size() == 0){
                return false;
            }

            this.cartItemRepository.deleteAll(items);

            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAll(List<CartItem> items){
        try{
            this.cartItemRepository.deleteAll(items);
            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
