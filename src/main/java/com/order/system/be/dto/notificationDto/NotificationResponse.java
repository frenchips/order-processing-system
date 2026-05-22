package com.order.system.be.dto.notificationDto;

<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {
=======
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
>>>>>>> 7a126d3d81564ddcf1a31431a679aded9c841fc0
}
