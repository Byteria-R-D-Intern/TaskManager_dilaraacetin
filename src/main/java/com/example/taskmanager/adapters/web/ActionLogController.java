package com.example.taskmanager.adapters.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.adapters.web.dto.NotificationResponse;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.application.usecases.NotificationService;
import com.example.taskmanager.application.usecases.NotificationService.MarkResult;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/logs")
public class ActionLogController {

    private final ActionLogService logService;
    private final NotificationService notificationService;

    public ActionLogController(ActionLogService logService,
                               NotificationService notificationService) {
        this.logService = logService;
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {
        Long targetUserId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationsForTargetUser(targetUserId));
    }

    @PostMapping("/notifications/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, Authentication auth) {
        Long me = (Long) auth.getPrincipal();
        MarkResult result = notificationService.markAsReadStrict(id, me);

        switch (result) {
            case UPDATED: {
                NotificationResponse dto = notificationService.getNotificationForTarget(id, me);
                return ResponseEntity.ok(dto); 
            }
            case FORBIDDEN:
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "error", "FORBIDDEN",
                                "message", "You cannot mark this notification as read.",
                                "id", id
                        ));
            case NOT_FOUND:
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "NOT_FOUND",
                                "message", "Notification not found.",
                                "id", id
                        ));
        }
    }

    @PostMapping("/notifications/read-all")
    public ResponseEntity<Map<String, Object>> markAllRead(Authentication auth) {
        Long me = (Long) auth.getPrincipal();
        int updated = notificationService.markAllAsRead(me);
        return ResponseEntity.ok(Map.of(
                "updated", updated,
                "status", "OK"
        ));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ActionLogResponse>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<ActionLogResponse>> getLogsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(logService.getLogsByUserId(userId));
    }
}
