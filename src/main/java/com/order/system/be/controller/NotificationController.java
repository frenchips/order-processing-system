package com.order.system.be.controller;

import com.order.system.be.dto.notificationDto.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @GetMapping("/notifications")
    public ResponseEntity<NotificationResponse> getListNotification(){
        return null;
    }
}
