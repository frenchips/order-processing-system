package com.order.system.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Table(name = "notifications")
@Entity
@Getter
@Setter
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "message")
    private String notificationMessage;

    @Column(name = "is_read")
    private String isRead;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
