package com.example.taskmanager.adapters.web.dto;

import jakarta.validation.constraints.NotNull;

public class TaskDeleteRequest {

    @NotNull(message = "Task ID is required")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
