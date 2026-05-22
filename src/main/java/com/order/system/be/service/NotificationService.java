package com.order.system.be.service;

import com.order.system.be.dto.notificationDto.NotificationResponse;
import com.order.system.be.dto.productDto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    public List<NotificationResponse> getUserNotifications(String userId);
}
