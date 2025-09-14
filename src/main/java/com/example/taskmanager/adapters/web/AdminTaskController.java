package com.example.taskmanager.adapters.web;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.AdminCreateTaskRequest;
import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.NotificationRepository;
import com.example.taskmanager.domain.ports.TaskRepository;
import com.example.taskmanager.domain.ports.UserRepository;
import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import com.example.taskmanager.infrastructure.repository.ActionLogRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/tasks")
@Validated
public class AdminTaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActionLogRepository actionLogRepository;
    private final NotificationRepository notificationRepository;

    public AdminTaskController(TaskRepository taskRepository,
                               UserRepository userRepository,
                               ActionLogRepository actionLogRepository,
                               NotificationRepository notificationRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.actionLogRepository = actionLogRepository;
        this.notificationRepository = notificationRepository;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Task> createForUser(@Valid @RequestBody AdminCreateTaskRequest req,
                                              Authentication auth) {

        final var user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + req.getUserId()));


        Task toSave = new Task(
            null,
            req.getTitle(),
            req.getDescription(),
            req.getStatus(),
            req.getPriority(),
            req.getDueDate(),
            user.getId()
        );
        Task saved = taskRepository.save(toSave);


        Long actorUserId = (Long) auth.getPrincipal();
        ActionLogEntity log = new ActionLogEntity(
            actorUserId,
            "CREATE",
            "Task",
            saved.getId(),
            LocalDateTime.now()
        );
        log.setTargetUserId(user.getId());
        log.setDetails("{\"title\":\"" + escape(req.getTitle()) + "\",\"priority\":\"" + req.getPriority() + "\"}");
        ActionLogEntity persistedLog = actionLogRepository.save(log);

        Notification n = new Notification(
            null,
            actorUserId,
            user.getId(),
            "TASK_CREATE",
            "Task created (ID: " + saved.getId() + ")",
            req.getTitle(),
            req.getPriority().name(), 
            false,
            null,
            persistedLog.getId(),
            LocalDateTime.now()
        );
        notificationRepository.save(n);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    private static String escape(String s){
        if (s == null) return "";
        return s.replace("\\","\\\\").replace("\"","\\\"");
    }
}
