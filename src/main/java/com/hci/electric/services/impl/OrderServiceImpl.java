package com.hci.electric.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Order;
import com.hci.electric.repositories.OrderRepository;
import com.hci.electric.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public Order saveRecord(Order record, boolean reviewed){
        try{
            record.setReviewed(reviewed);

            return this.orderRepository.save(record);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> getByBillId(String billId){
        try{
            Optional<List<Order>> items = this.orderRepository.getOrdersByBill(billId);
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
    public List<Order> getByBillAndReviewedStatus(String billId, boolean reviewed){
        try{
            Optional<List<Order>> orders = this.orderRepository.getByBillAndReviewedStatus(billId, reviewed);
            if (orders.isPresent() == false){
                return new ArrayList<>();
            }

            return orders.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public Order getById(Integer id){
        try{
            Optional<Order> order = this.orderRepository.getById(id);
            if (order.isPresent() == false){
                return null;
            }
            return order.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> getByProductId(String productId){
        try{
            Optional<List<Order>> orders = this.orderRepository.getByProductId(productId);
            if (orders.isPresent() == false){
                return new ArrayList<>();
            }

            return orders.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

}
