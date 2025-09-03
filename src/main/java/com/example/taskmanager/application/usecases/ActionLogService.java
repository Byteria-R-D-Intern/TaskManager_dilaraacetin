package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.adapters.web.dto.ActionLogResponse;
import com.example.taskmanager.adapters.web.dto.MyActionLogResponse;
import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;

@Service
public class ActionLogService {

    private final ActionLogRepository repository;

    public ActionLogService(ActionLogRepository repository) {
        this.repository = repository;
    }

    public void log(Long actorUserId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(actorUserId, action, resource, resourceId, LocalDateTime.now());
        repository.save(log);
    }

    public void log(Long actorUserId, Long targetUserId, String action, String resource, Long resourceId) {
        ActionLogEntity log = new ActionLogEntity(actorUserId, action, resource, resourceId, LocalDateTime.now());
        log.setTargetUserId(targetUserId);
        repository.save(log);
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
        List<ActionLogEntity> logs = repository
                .findByActorUserIdOrTargetUserIdOrderByTimestampDesc(userId, userId);

        return logs.stream()
                .map(log -> new MyActionLogResponse(
                        log.getId(),
                        log.getActorUserId(),
                        log.getTargetUserId(),
                        log.getAction(),
                        log.getResource(),
                        log.getResourceId(),
                        log.getTimestamp()))
                .toList();
    }
}
