package com.example.taskmanager.adapters.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.adapters.web.dto.NotificationResponse;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.application.usecases.NotificationService;

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
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ActionLogResponse>> getAllLogs() {
        List<ActionLogResponse> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<ActionLogResponse>> getLogsByUserId(@PathVariable Long userId) {
        List<ActionLogResponse> logs = logService.getLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }
}
