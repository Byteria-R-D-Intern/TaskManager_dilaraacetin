package com.example.taskmanager.adapters.web;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.application.usecases.ActionLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@PreAuthorize("hasRole('ROLE_ADMIN')") // sadece ADMIN kullanıcılar erişebilir
public class ActionLogController {

    private final ActionLogService logService;

    public ActionLogController(ActionLogService logService) {
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<List<ActionLogResponse>> getAllLogs() {
        List<ActionLogResponse> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ActionLogResponse>> getLogsByUserId(@PathVariable Long userId) {
        List<ActionLogResponse> logs = logService.getLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }
}
