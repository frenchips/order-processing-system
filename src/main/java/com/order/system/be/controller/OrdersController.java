package com.order.system.be.controller;

import com.order.system.be.dto.orderDto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrdersController {

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> create(){
        return null;
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable("id") Long id){
        return null;
    }
}
