package com.example.taskmanager.adapters.web;

import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ActionLogController {

    private final ActionLogRepository actionLogRepository;

    public ActionLogController(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    @GetMapping("/my")
    public ResponseEntity<List<ActionLogEntity>> getMyLogs(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<ActionLogEntity> logs = actionLogRepository.findByUserId(userId);
        return ResponseEntity.ok(logs);
    }
}
