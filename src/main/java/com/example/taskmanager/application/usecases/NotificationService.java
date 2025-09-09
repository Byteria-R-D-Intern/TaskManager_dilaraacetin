package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.adapters.web.dto.NotificationResponse;
import com.example.taskmanager.domain.exception.NotFoundException;
import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.domain.ports.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsForTargetUser(Long targetUserId) {
        return repo.findByTargetUserIdOrderByCreatedAtDesc(targetUserId)
                .stream()
                .filter(n -> n.getActorUserId() == null || !n.getActorUserId().equals(n.getTargetUserId()))
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Notification> listForTargetUser(Long targetUserId) {
        return repo.findByTargetUserIdOrderByCreatedAtDesc(targetUserId)
                .stream()
                .filter(n -> n.getActorUserId() == null || !n.getActorUserId().equals(n.getTargetUserId()))
                .toList();
    }

    @Transactional
    public void markAsRead(Long notificationId, Long targetUserId) {
        Notification n = repo.findByIdAndTargetUserId(notificationId, targetUserId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        if (!n.isRead()) {
            n.setRead(true);
            n.setReadAt(LocalDateTime.now());
            repo.save(n);
        }
    }

    @Transactional
    public void markAllAsRead(Long targetUserId) {
        repo.markAllAsReadByTargetUserId(targetUserId, LocalDateTime.now());
    }

    private NotificationResponse toDto(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getActorUserId(),
                n.getTargetUserId(),
                n.getType(),
                n.getTitle(),
                n.getBody(),
                n.getPriority(),
                n.isRead(),
                n.getReadAt(),
                n.getSourceLogId(),
                n.getCreatedAt()
        );
    }
}
