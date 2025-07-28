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
            task.isCompleted(),
            task.getUserId()
        );
    }

    public Task toDomain(TaskEntity entity) {
        return new Task(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.isCompleted(),
            entity.getUserId()
        );
    }
}
