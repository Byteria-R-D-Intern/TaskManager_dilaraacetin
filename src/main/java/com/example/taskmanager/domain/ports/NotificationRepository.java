package com.example.taskmanager.domain.ports;

import java.util.List;
import com.example.taskmanager.domain.model.Notification;

public interface NotificationRepository {
    Notification save(Notification n);  // <<< EKLENDİ
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
