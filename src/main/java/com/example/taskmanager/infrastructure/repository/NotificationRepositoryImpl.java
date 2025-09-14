package com.example.taskmanager.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Notification save(Notification notification) {
        return NotificationMapper.toDomain(
            jpa.save(NotificationMapper.toEntity(notification))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findById(Long id) {
        return jpa.findById(id).map(NotificationMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notification> findByIdAndTargetUserId(Long id, Long targetUserId) {
        return jpa.findByIdAndTargetUserId(id, targetUserId)
                  .map(NotificationMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId) {
        return jpa.findByTargetUserIdOrderByCreatedAtDesc(targetUserId)
                  .stream()
                  .map(NotificationMapper::toDomain)
                  .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int markAllAsReadByTargetUserId(Long targetUserId, LocalDateTime readAt) {
        return jpa.markAllAsReadByTargetUserId(targetUserId, readAt);
    }
}
