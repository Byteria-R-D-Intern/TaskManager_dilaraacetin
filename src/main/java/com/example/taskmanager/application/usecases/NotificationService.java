package com.example.taskmanager.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.adapters.web.dto.NotificationResponse;
import com.example.taskmanager.domain.ports.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public List<NotificationResponse> getNotificationsForUser(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId)
                   .stream()
                   .map(n -> new NotificationResponse(
                       n.getId(),
                       n.getUserId(),
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
                   ))
                   .toList();
    }
}
