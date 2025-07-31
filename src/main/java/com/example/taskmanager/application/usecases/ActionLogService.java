package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;

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
}