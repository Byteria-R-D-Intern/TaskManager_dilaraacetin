package com.example.taskmanager.application.usecases;

import java.util.List;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.TaskRepository;

public class GetTasksByUserUseCase {

    private final TaskRepository taskRepository;

    public GetTasksByUserUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }
}
