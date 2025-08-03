package com.example.taskmanager.application.usecases;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActionLogService {

    private final ActionLogRepository repository;

    public ActionLogService(ActionLogRepository repository) {
        this.repository = repository;
    }

    public void log(Long userId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(userId, action, resource, resourceId, LocalDateTime.now());
        repository.save(log);
    }

    public List<ActionLogResponse> getAllLogs() {
        return repository.findAll().stream()
                .map(log -> new ActionLogResponse(
                        log.getId(),
                        log.getUserId(),
                        log.getAction(),
                        log.getResource(),
                        log.getResourceId(),
                        log.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<ActionLogResponse> getLogsByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(log -> new ActionLogResponse(
                        log.getId(),
                        log.getUserId(),
                        log.getAction(),
                        log.getResource(),
                        log.getResourceId(),
                        log.getTimestamp()))
                .collect(Collectors.toList());
    }
}
