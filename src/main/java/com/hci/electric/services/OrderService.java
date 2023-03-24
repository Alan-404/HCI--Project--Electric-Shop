package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Order;

public interface OrderService {
    public Order saveRecord(Order record);
    public List<Order> getByBillId(String billId);
}
