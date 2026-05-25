package com.order.system.be.service;

import com.order.system.be.dto.notificationDto.NotificationResponse;
import com.order.system.be.entity.Notifications;
import com.order.system.be.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void getUserNotifications_Success() {
        Notifications notification = new Notifications();
        notification.setUserId("1");
        notification.setMessage("Test Message");
        notification.setIsRead(false);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc("1"))
                .thenReturn(Collections.singletonList(notification));

        List<NotificationResponse> responses = notificationService.getUserNotifications("1");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test Message", responses.get(0).getMessage());
    }
}
