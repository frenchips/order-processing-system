package com.order.system.be.controller;

import com.order.system.be.dto.orderDto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> create(){
        return null;
    }
}
