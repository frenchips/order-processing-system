package com.order.system.be.service;

import com.order.system.be.dto.notificationDto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    public List<NotificationResponse> getUserNotifications(String userId);
}
