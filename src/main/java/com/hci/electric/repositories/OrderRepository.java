package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Order;
import com.hci.electric.utils.queries.OrderQuery;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = OrderQuery.queryGetOrdersByBill, nativeQuery = true)
    Optional<List<Order>> getOrdersByBill(String billId);

    @Query(value = OrderQuery.queryGetOrderByBillAndReviewStatus, nativeQuery = true)
    Optional<List<Order>> getByBillAndReviewedStatus (String billId, boolean reviewed);

    @Query(value = OrderQuery.queryGetById, nativeQuery = true)
    Optional<Order> getById(Integer id);

    @Query(value = OrderQuery.queryGetOrdersByProductId, nativeQuery = true)
    Optional<List<Order>> getByProductId(String productId);
}