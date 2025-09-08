package com.example.taskmanager.infrastructure.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.taskmanager.infrastructure.entity.NotificationEntity;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
