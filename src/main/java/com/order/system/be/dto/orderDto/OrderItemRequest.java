package com.order.system.be.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemRequest {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Integer price;
}
