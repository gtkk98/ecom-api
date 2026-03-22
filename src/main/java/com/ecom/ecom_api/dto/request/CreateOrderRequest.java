package com.ecom.ecom_api.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long id;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
