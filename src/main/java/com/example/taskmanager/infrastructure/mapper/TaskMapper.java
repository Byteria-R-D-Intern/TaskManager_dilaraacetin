package com.example.taskmanager.infrastructure.mapper;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.infrastructure.entity.TaskEntity;

public class TaskMapper {

    public static TaskEntity toEntity(Task task) {
        return new TaskEntity(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getUserId()
        );
    }

    public static Task toDomain(TaskEntity entity) {
        return new Task(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.isCompleted(),
            entity.getUserId()
        );
    }
}
