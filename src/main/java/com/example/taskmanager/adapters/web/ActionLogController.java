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
import com.example.taskmanager.adapters.web.dto.MyActionLogResponse;
import com.example.taskmanager.application.usecases.ActionLogService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/logs")

public class ActionLogController {

    private final ActionLogService logService;

    public ActionLogController(ActionLogService logService) {
        this.logService = logService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<MyActionLogResponse>> getMyLogs(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<MyActionLogResponse> logs = logService.getMyLogs(userId);
        return ResponseEntity.ok(logs);
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
