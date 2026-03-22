package com.ecom.ecom_api.controller;

import com.ecom.ecom_api.dto.request.CreateOrderRequest;
import com.ecom.ecom_api.dto.response.OrderResponse;
import com.ecom.ecom_api.model.Order;
import com.ecom.ecom_api.model.OrderItem;
import com.ecom.ecom_api.model.OrderStatus;
import com.ecom.ecom_api.model.Product;
import com.ecom.ecom_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(
                orderService.getAllOrders().stream()
                        .map(this::mapToResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(mapToResponse(orderService.getOrderById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrderByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId).stream()
                        .map(this::mapToResponse)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody CreateOrderRequest request) {

        List<OrderItem> items = request.getItems().stream()
                .map(itemReq -> {
                    OrderItem item = new OrderItem();
                    Product product = new Product();
                    product.setId(itemReq.getProductId());
                    item.setProduct(product);
                    item.setQuantity(itemReq.getQuantity());
                    return item;
                })
                .toList();

        Order order = orderService.createOrder(request.getUserId(), items);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(order));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(
                mapToResponse(orderService.updatedOrderStatus(id, status))
        );
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUserName(order.getUser().getName());

        List<OrderResponse.OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(item -> {
                    OrderResponse.OrderItemResponse ir = new OrderResponse.OrderItemResponse();
                    ir.setProductId(item.getProduct().getId());
                    ir.setProductName(item.getProduct().getName());
                    ir.setQuantity(item.getQuantity());
                    ir.setUnitPrice(item.getUnitPrice());
                    return ir;
                })

                .toList();

        response.setItems(itemResponses);
        return response;
    }
}
