package com.example.taskmanager.domain.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.taskmanager.domain.model.Notification;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findByIdAndTargetUserId(Long id, Long targetUserId);

    List<Notification> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId);

    int markAllAsReadByTargetUserId(Long targetUserId, LocalDateTime readAt);
}
