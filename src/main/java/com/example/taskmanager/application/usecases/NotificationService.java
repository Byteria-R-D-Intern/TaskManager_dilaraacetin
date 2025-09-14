package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.adapters.web.dto.NotificationResponse;
import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.NotificationRepository;
import com.example.taskmanager.domain.ports.UserRepository;

@Service
public class NotificationService {

    public enum MarkResult { UPDATED, FORBIDDEN, NOT_FOUND }

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsForTargetUser(Long targetUserId) {
        return notificationRepository.findByTargetUserIdOrderByCreatedAtDesc(targetUserId)
            .stream()
            .filter(n -> !Objects.equals(n.getActorUserId(), n.getTargetUserId()))
            .map(this::toDtoWithUsernames)
            .collect(Collectors.toList());
    }

    @Transactional
    public MarkResult markAsReadStrict(Long id, Long targetUserId) {
        return notificationRepository.findByIdAndTargetUserId(id, targetUserId)
            .map(n -> {
                if (n.isUnread()) {
                    n.markAsRead(LocalDateTime.now());
                    notificationRepository.save(n);
                }
                return MarkResult.UPDATED;
            })
            .orElseGet(() -> notificationRepository.findById(id).isPresent()
                ? MarkResult.FORBIDDEN
                : MarkResult.NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationForTarget(Long id, Long targetUserId) {
        return notificationRepository.findByIdAndTargetUserId(id, targetUserId)
                .map(this::toDtoWithUsernames)
                .orElse(null);
    }

    @Transactional
    public int markAllAsRead(Long targetUserId) {
        return notificationRepository.markAllAsReadByTargetUserId(targetUserId, LocalDateTime.now());
    }

    private NotificationResponse toDtoWithUsernames(Notification n) {
        String actorUsername  = usernameOf(n.getActorUserId());
        String targetUsername = usernameOf(n.getTargetUserId());
        return new NotificationResponse(
            n.getId(),
            n.getActorUserId(),
            actorUsername,
            n.getTargetUserId(),
            targetUsername,
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

    private String usernameOf(Long userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .map(User::getUsername)
                .orElse(null);
    }
}
