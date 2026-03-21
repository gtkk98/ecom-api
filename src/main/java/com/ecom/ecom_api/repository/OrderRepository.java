package com.ecom.ecom_api.repository;

import com.ecom.ecom_api.model.Order;
import com.ecom.ecom_api.model.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository {

    List<Order> findByUserId(Long userId);

    List<Order> findByStatus(OrderStatus status);
}
