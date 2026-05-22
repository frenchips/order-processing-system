package com.order.system.be.repository;

import com.order.system.be.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    List<Notifications> findByUserIdOrderByCreatedAtDesc(String userId);
}
