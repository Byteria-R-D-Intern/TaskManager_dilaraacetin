package com.example.taskmanager.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.domain.ports.NotificationRepository;
import com.example.taskmanager.infrastructure.mapper.NotificationMapper;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JpaNotificationRepository jpa;

    public NotificationRepositoryImpl(JpaNotificationRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Notification save(Notification n) {        
        var saved = jpa.save(NotificationMapper.toEntity(n));
        return NotificationMapper.toDomain(saved);
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return jpa.findByUserIdOrderByCreatedAtDesc(userId)
                  .stream()
                  .map(NotificationMapper::toDomain)
                  .collect(Collectors.toList());
    }
}
