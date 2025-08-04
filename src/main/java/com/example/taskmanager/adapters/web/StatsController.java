package com.example.taskmanager.adapters.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.TaskStatisticsResponse;
import com.example.taskmanager.application.usecases.TaskStatsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final TaskStatsService taskStatsService;

    public StatsController(TaskStatsService taskStatsService) {
        this.taskStatsService = taskStatsService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<TaskStatisticsResponse> getMyStats(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        TaskStatisticsResponse stats = taskStatsService.getStatsForUser(userId);
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @GetMapping("/tasks/all")
    public ResponseEntity<TaskStatisticsResponse> getAllStats() {
        TaskStatisticsResponse stats = taskStatsService.getStatsForAll();
        return ResponseEntity.ok(stats);
    }
}
