package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Order;

public interface OrderService {
    public Order saveRecord(Order record, boolean reviewed);
    public List<Order> getByBillId(String billId);
    public List<Order> getByBillAndReviewedStatus(String billId, boolean reviewed);
    public Order getById(Integer id);
    public List<Order> getByProductId(String productId);
}
