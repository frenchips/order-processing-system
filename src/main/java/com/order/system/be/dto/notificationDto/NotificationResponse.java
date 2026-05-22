package com.order.system.be.dto.notificationDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class NotificationResponse {
    private String userId;
    private String message;
    private Boolean isRead;
    private Timestamp createdAt;
}
