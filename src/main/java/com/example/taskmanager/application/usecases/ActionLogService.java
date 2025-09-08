package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.adapters.web.dto.MyActionLogResponse;
import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.domain.ports.NotificationRepository;
import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;

@Service
public class ActionLogService {

    private final ActionLogRepository repository;
    private final NotificationRepository notificationRepository;

    public ActionLogService(ActionLogRepository repository,
                            NotificationRepository notificationRepository) {
        this.repository = repository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void log(Long actorUserId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(actorUserId, action, resource, resourceId, LocalDateTime.now());
        ActionLogEntity saved = repository.save(log);
        createNotificationFor(saved, saved.getActorUserId(), null);
    }

    @Transactional
    public void log(Long actorUserId, Long targetUserId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(actorUserId, action, resource, resourceId, LocalDateTime.now());
        log.setTargetUserId(targetUserId);
        ActionLogEntity saved = repository.save(log);
        createNotificationFor(saved, saved.getActorUserId(), targetUserId);
        if (targetUserId != null && !targetUserId.equals(actorUserId)) {
            createNotificationFor(saved, targetUserId, targetUserId);
        }
    }

    private void createNotificationFor(ActionLogEntity log, Long recipientUserId, Long targetUserId) {
        String type = ((log.getResource() == null ? "" : log.getResource()) + "_" +
                       (log.getAction() == null ? "" : log.getAction())).toUpperCase();

        String title = buildTitle(log.getAction(), log.getResource(), log.getResourceId());
        String priority = switch (safeUpper(log.getAction())) {
            case "DELETE", "REMOVE" -> "HIGH";
            case "UPDATE", "PUT", "PATCH" -> "MEDIUM";
            default -> "LOW";
        };

        Notification n = new Notification(
            null,
            recipientUserId,
            log.getActorUserId(),
            targetUserId,           
            type,
            title,
            null,                   
            priority,
            false,
            null,
            log.getId(),
            log.getTimestamp() != null ? log.getTimestamp() : LocalDateTime.now()
        );
        notificationRepository.save(n);
    }

    private String safeUpper(String s) { return s == null ? "" : s.toUpperCase(); }

    private String buildTitle(String action, String resource, Long resId) {
        String r = resource != null ? resource : "Resource";
        String a = action != null ? action.toLowerCase() : "action";
        return resId != null ? (r + " " + a + " (ID: " + resId + ")") : (r + " " + a);
    }

    public List<ActionLogResponse> getAllLogs() {  return repository.findAll().stream()
        .map(log -> new ActionLogResponse(
            log.getId(),
            log.getActorUserId(),
            log.getAction(),
            log.getResource(),
            log.getResourceId(),
            log.getTimestamp()))
        .toList();
    }

    public List<ActionLogResponse> getLogsByUserId(Long userId) {  return repository
        .findByActorUserIdOrderByTimestampDesc(userId).stream()
        .map(log -> new ActionLogResponse(
            log.getId(),
            log.getActorUserId(),
            log.getAction(),
            log.getResource(),
            log.getResourceId(),
            log.getTimestamp()))
        .toList();
    }

    public List<MyActionLogResponse> getMyLogs(Long userId) { 
        var logs = repository.findByActorUserIdOrTargetUserIdOrderByTimestampDesc(userId, userId);
        return logs.stream().map(log -> new MyActionLogResponse(
            log.getId(), log.getActorUserId(), log.getTargetUserId(),
            log.getAction(), log.getResource(), log.getResourceId(), log.getTimestamp()))
        .toList();
    }
}
