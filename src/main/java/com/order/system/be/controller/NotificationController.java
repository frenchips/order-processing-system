package com.order.system.be.controller;

import com.order.system.be.dto.notificationDto.NotificationResponse;
import com.order.system.be.entity.Notifications;
import com.order.system.be.repository.NotificationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NotificationController {
    private final NotificationRepository notificationsRepository;

    public NotificationController(NotificationRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    @GetMapping
    public List<NotificationResponse> getUserNotifications(@RequestParam("userId") String userId) {
        List<Notifications> notificationsList = notificationsRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notificationsList.stream().map(notif -> new NotificationResponse(
                notif.getUserId(),
                notif.getMessage(),
                notif.getIsRead(),
                notif.getCreatedAt()
        )).collect(Collectors.toList());
    }
}
