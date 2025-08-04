package com.example.taskmanager.adapters.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.TaskCreateRequest;
import com.example.taskmanager.adapters.web.dto.TaskResponse;
import com.example.taskmanager.adapters.web.dto.TaskUpdateRequest;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.application.usecases.CreateTaskUseCase;
import com.example.taskmanager.application.usecases.GetTasksByUserUseCase;
import com.example.taskmanager.application.usecases.UpdateTaskUseCase;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.DeleteTaskUseCase;
import com.example.taskmanager.domain.ports.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTasksByUserUseCase getTasksByUserUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final ActionLogService logService;
    private final UserRepository userRepository;

    public TaskController(
            CreateTaskUseCase createTaskUseCase,
            GetTasksByUserUseCase getTasksByUserUseCase,
            UpdateTaskUseCase updateTaskUseCase,
            DeleteTaskUseCase deleteTaskUseCase,
            ActionLogService logService,
            UserRepository userRepository) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTasksByUserUseCase = getTasksByUserUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.logService = logService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request,
                                                   Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        Task task = new Task(
            null,
            request.getTitle(),
            request.getDescription(),
            request.getStatus(),
            request.getPriority(),
            request.getDueDate(),
            userId
        );

        Task created = createTaskUseCase.create(task);
        logService.log(userId, "CREATE", "Task", created.getId());

        TaskResponse response = new TaskResponse(
            created.getId(),
            created.getTitle(),
            created.getDescription(),
            created.getStatus(),
            created.getPriority(),
            created.getDueDate(),
            created.getUserId(),
            getUsernameById(created.getUserId())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String title,
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        List<Task> tasks = getTasksByUserUseCase.getTasksForUser(userId);

        if (status != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        if (priority != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getPriority().name().equalsIgnoreCase(priority))
                    .toList();
        }

        if (title != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        List<TaskResponse> responseList = tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getDueDate(),
                        task.getUserId(),
                        getUsernameById(task.getUserId())
                ))
                .toList();

        return ResponseEntity.ok(responseList);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                        @Valid @RequestBody TaskUpdateRequest request,
                                        Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        String userRole = SecurityContextHolder.getContext().getAuthentication()
                                                .getAuthorities().stream()
                                                .findFirst().map(Object::toString).orElse("");

        Optional<Task> taskOpt = updateTaskUseCase.getById(id);

        if (taskOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = taskOpt.get();

        boolean isOwner = task.getUserId().equals(userId);
        boolean isPrivileged = userRole.equals("ROLE_MANAGER") || userRole.equals("ROLE_ADMIN");

        if (!isOwner && !isPrivileged) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updated = updateTaskUseCase.update(task);
        logService.log(userId, "UPDATE", "Task", updated.getId());

        TaskResponse response = new TaskResponse(
            updated.getId(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPriority(),
            updated.getDueDate(),
            updated.getUserId(),
            getUsernameById(updated.getUserId())
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id,
                                        Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        String userRole = SecurityContextHolder.getContext().getAuthentication()
                                                .getAuthorities().stream()
                                                .findFirst().map(Object::toString).orElse("");

        Optional<Task> taskOpt = deleteTaskUseCase.getById(id);

        if (taskOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = taskOpt.get();

        boolean isOwner = task.getUserId().equals(userId);
        boolean isPrivileged = userRole.equals("ROLE_MANAGER") || userRole.equals("ROLE_ADMIN");

        if (!isOwner && !isPrivileged) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        deleteTaskUseCase.delete(task.getId());
        logService.log(userId, "DELETE", "Task", task.getId());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = getTasksByUserUseCase.getAllTasks();

        List<TaskResponse> responseList = tasks.stream()
            .map(task -> new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getUserId(),
                getUsernameById(task.getUserId())
            ))
            .toList();

        return ResponseEntity.ok(responseList);
    }

    private String getUsernameById(Long userId) {
        return userRepository.findById(userId)
                             .map(User::getUsername)
                             .orElse("Unknown");
    }
}
