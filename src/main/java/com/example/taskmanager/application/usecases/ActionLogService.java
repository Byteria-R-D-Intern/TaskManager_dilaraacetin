package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.adapters.web.dto.MyActionLogResponse;
import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.NotificationRepository;
import com.example.taskmanager.domain.ports.TaskRepository;
import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;

@Service
public class ActionLogService {

    private final ActionLogRepository repository;
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;

    public ActionLogService(ActionLogRepository repository,
                            NotificationRepository notificationRepository,
                            TaskRepository taskRepository) {
        this.repository = repository;
        this.notificationRepository = notificationRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void log(Long actorUserId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(actorUserId, action, resource, resourceId, LocalDateTime.now());

        Long autoTarget = resolveTargetUserId(resource, resourceId);
        if (autoTarget != null) {
            log.setTargetUserId(autoTarget);
        }

        ActionLogEntity saved = repository.save(log);

        if (saved.getTargetUserId() != null) {
            createNotificationToTarget(saved);
        }
    }

    @Transactional
    public void log(Long actorUserId, Long targetUserId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(actorUserId, action, resource, resourceId, LocalDateTime.now());
        log.setTargetUserId(targetUserId);
        ActionLogEntity saved = repository.save(log);

        if (targetUserId != null) {
            createNotificationToTarget(saved);
        }
    }

    private Long resolveTargetUserId(String resource, Long resourceId) {
        String r = safeUpper(resource);
        if (resourceId == null) return null;

        if ("TASK".equals(r)) {
            Optional<Task> t = taskRepository.findById(resourceId);
            return t.map(Task::getUserId).orElse(null);
        }
        if ("USER".equals(r)) {
            return resourceId;
        }
        return null;
    }

    private void createNotificationToTarget(ActionLogEntity log) {
        String type = ((log.getResource() == null ? "" : log.getResource()) + "_" +
                       (log.getAction()   == null ? "" : log.getAction()))
                       .toUpperCase();

        String title = buildTitle(log.getAction(), log.getResource(), log.getResourceId());

        String priority = switch (safeUpper(log.getAction())) {
            case "DELETE", "REMOVE" -> "HIGH";
            case "UPDATE", "PUT", "PATCH" -> "MEDIUM";
            default -> "LOW";
        };

        String body = (title == null || title.isBlank()) ? "Notification" : title;
        try {
            var details = log.getDetails();
            if (details != null) {
                String s = String.valueOf(details);
                if (s.length() > 500) s = s.substring(0, 500) + " …";
                body = s;
            }
        } catch (Exception ignore) { }

        Notification n = new Notification(
            null,
            log.getActorUserId(),             
            log.getTargetUserId(),           
            type,
            (title == null || title.isBlank()) ? "Notification" : title,
            body,
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


    public List<ActionLogResponse> getAllLogs() {
        return repository.findAll().stream()
            .map(log -> new ActionLogResponse(
                log.getId(),
                log.getActorUserId(),
                log.getAction(),
                log.getResource(),
                log.getResourceId(),
                log.getTimestamp()))
            .toList();
    }

    public List<ActionLogResponse> getLogsByUserId(Long userId) {
        return repository.findByActorUserIdOrderByTimestampDesc(userId).stream()
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
