package com.ecom.ecom_api.service;

import com.ecom.ecom_api.model.*;
import com.ecom.ecom_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order createOrder(Long userId, List<OrderItem> items) {
        User user = userService.getUserById(userId);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem item : items) {
            Product product = productService.reduceStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
            item.setUnitPrice(product.getPrice());
            item.setOrder(order);

            total = total.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(items);
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updatedOrderStatus(Long id, OrderStatus newStatus) {
        Order order = getOrderById(id);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
