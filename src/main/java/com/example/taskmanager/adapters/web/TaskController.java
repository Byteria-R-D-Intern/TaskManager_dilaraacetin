package com.example.taskmanager.adapters.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.TaskCreateRequest;
import com.example.taskmanager.adapters.web.dto.TaskDeleteRequest;
import com.example.taskmanager.adapters.web.dto.TaskResponse;
import com.example.taskmanager.adapters.web.dto.TaskUpdateRequest;
import com.example.taskmanager.application.usecases.CreateTaskUseCase;
import com.example.taskmanager.application.usecases.GetTasksByUserUseCase;
import com.example.taskmanager.application.usecases.UpdateTaskUseCase;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.DeleteTaskUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTasksByUserUseCase getTasksByUserUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;


    public TaskController(CreateTaskUseCase createTaskUseCase,
                      GetTasksByUserUseCase getTasksByUserUseCase,
                      UpdateTaskUseCase updateTaskUseCase,
                      DeleteTaskUseCase deleteTaskUseCase) {
    this.createTaskUseCase = createTaskUseCase;
    this.getTasksByUserUseCase = getTasksByUserUseCase;
    this.updateTaskUseCase = updateTaskUseCase;
    this.deleteTaskUseCase = deleteTaskUseCase;
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

        TaskResponse response = new TaskResponse(
            created.getId(),
            created.getTitle(),
            created.getDescription(),
            created.getStatus(),
            created.getPriority(),
            created.getDueDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        List<Task> tasks = getTasksByUserUseCase.getTasksForUser(userId);

        List<TaskResponse> responseList = tasks.stream()
            .map(task -> new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate()
            ))
            .toList();

        return ResponseEntity.ok(responseList);
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@Valid @RequestBody TaskUpdateRequest request,
                                        Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Long taskId = request.getId();

        Optional<Task> taskOpt = updateTaskUseCase.getById(taskId);

        if (taskOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = taskOpt.get();

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task updated = updateTaskUseCase.update(task);

        TaskResponse response = new TaskResponse(
            updated.getId(),
            updated.getTitle(),
            updated.getDescription(),
            updated.getStatus(),
            updated.getPriority(),
            updated.getDueDate()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@Valid @RequestBody TaskDeleteRequest request,
                                        Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Long taskId = request.getId();

        Optional<Task> taskOpt = deleteTaskUseCase.getById(taskId);

        if (taskOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        Task task = taskOpt.get();

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        deleteTaskUseCase.delete(taskId);
        return ResponseEntity.noContent().build();
    }




}
