package com.example.taskmanager.adapters.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.CommentRequest;
import com.example.taskmanager.adapters.web.dto.CommentResponse;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.domain.model.Comment;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.CommentRepository;
import com.example.taskmanager.domain.ports.TaskRepository;
import com.example.taskmanager.domain.ports.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActionLogService logService;

    public CommentController(CommentRepository commentRepository,
                             TaskRepository taskRepository,
                             UserRepository userRepository,
                             ActionLogService logService) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @GetMapping
    public ResponseEntity<?> list(@PathVariable Long taskId, Authentication auth) {
        Long callerId = (Long) auth.getPrincipal();
        String role = currentRole();

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");

        if (!canView(role, callerId, task)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        List<CommentResponse> out = commentRepository.findByTaskId(taskId)
                .stream().map(this::toDto).toList();

        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable Long taskId,
                                 @Valid @RequestBody CommentRequest req,
                                 Authentication auth) {
        Long callerId = (Long) auth.getPrincipal();
        String role = currentRole();

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");

        if (!canWrite(role, callerId, task)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Comment toSave = new Comment(null, taskId, callerId, req.getContent().trim(), LocalDateTime.now());
        Comment saved = commentRepository.save(toSave);

        // Log & bildirim (hedef: görev sahibi)
        logService.log(callerId, task.getUserId(), "CREATE", "Comment", taskId);

        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    // ---- helpers ----
    private boolean isPrivileged(String role) {
        return "ROLE_MANAGER".equals(role) || "ROLE_ADMIN".equals(role);
    }
    private boolean canView(String role, Long callerId, Task task) {
        if (isPrivileged(role)) return true;
        return task.getUserId().equals(callerId);
    }
    private boolean canWrite(String role, Long callerId, Task task) {
        // yazma kuralı = görüntüleme kuralı
        return canView(role, callerId, task);
    }
    private String currentRole() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().findFirst().map(Object::toString).orElse("");
    }
    private CommentResponse toDto(Comment c) {
        String username = userRepository.findById(c.getUserId())
                .map(User::getUsername).orElse("Unknown");
        return new CommentResponse(
                c.getId(), c.getTaskId(), c.getUserId(), username,
                c.getContent(), c.getTimestamp()
        );
    }
}
