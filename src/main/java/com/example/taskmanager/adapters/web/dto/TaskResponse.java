package com.example.taskmanager.adapters.web.dto;

import java.time.LocalDate;

import com.example.taskmanager.domain.model.TaskPriority;
import com.example.taskmanager.domain.model.TaskStatus;

public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Long userId;
    private String username;

    public TaskResponse(Long id, String title, String description, TaskStatus status,
                        TaskPriority priority, LocalDate dueDate, Long userId, String username) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
}
