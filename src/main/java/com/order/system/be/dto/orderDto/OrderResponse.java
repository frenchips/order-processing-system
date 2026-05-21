package com.order.system.be.dto.orderDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
    private String userId;
    private Integer totalAmount;
    private String status;
    private Timestamp createdAt;
    private List<OrderItemResponse> listOrderItems;
}
