package com.order.system.be.controller;

import com.order.system.be.dto.notificationDto.NotificationResponse;
import com.order.system.be.entity.Notifications;
import com.order.system.be.repository.NotificationRepository;
import com.order.system.be.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(@RequestParam("userId") String userId) {
        List<NotificationResponse> notificationsList = notificationService.getUserNotifications(userId);

        return ResponseEntity.status(HttpStatus.OK).body(notificationsList);
    }
}
