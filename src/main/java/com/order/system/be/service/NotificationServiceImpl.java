package com.order.system.be.service;

import com.order.system.be.dto.notificationDto.NotificationResponse;
import com.order.system.be.entity.Notifications;
import com.order.system.be.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService{

    private NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationResponse> getUserNotifications(String userId) {
        List<Notifications> notificationsList = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notificationsList.stream().map(notif -> new NotificationResponse(
                notif.getUserId(),
                notif.getMessage(),
                notif.getIsRead(),
                notif.getCreatedAt()
        )).collect(Collectors.toList());
    }


}
