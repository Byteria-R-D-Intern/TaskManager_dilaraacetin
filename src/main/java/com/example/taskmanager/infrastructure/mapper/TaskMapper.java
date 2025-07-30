package com.example.taskmanager.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.infrastructure.entity.TaskEntity;

@Component
public class TaskMapper {

    public TaskEntity toEntity(Task task) {
        return new TaskEntity(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getDueDate(),
            task.getUserId()
        );
    }

    public Task toDomain(TaskEntity entity) {
        return new Task(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getStatus(),
            entity.getPriority(),
            entity.getDueDate(),
            entity.getUserId()
        );
    }
}
