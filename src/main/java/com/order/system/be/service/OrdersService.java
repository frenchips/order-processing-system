package com.order.system.be.service;

import com.order.system.be.dto.orderDto.OrderCreatedEvent;
import com.order.system.be.dto.orderDto.OrderRequest;
import com.order.system.be.dto.orderDto.OrderResponse;

public interface OrdersService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse findOrderDetail(Long id);
    void executeOrderProcessing(OrderCreatedEvent event);
    void updateOrderStatusToFailed(Long orderId);
}
