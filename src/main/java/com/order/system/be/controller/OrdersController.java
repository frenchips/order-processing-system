package com.order.system.be.controller;

import com.order.system.be.dto.orderDto.OrderRequest;
import com.order.system.be.dto.orderDto.OrderResponse;
import com.order.system.be.service.OrdersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
        OrderResponse response = ordersService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable("id") Long id){
        OrderResponse response = ordersService.findOrderDetail(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
