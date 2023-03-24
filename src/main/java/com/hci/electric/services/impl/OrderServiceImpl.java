package com.hci.electric.services.impl;

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
    public Order saveRecord(Order record){
        try{
            record.setReviewed(false);

            return this.orderRepository.save(record);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

}
