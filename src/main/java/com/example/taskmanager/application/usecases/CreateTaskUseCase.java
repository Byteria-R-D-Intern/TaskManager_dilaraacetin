package com.example.taskmanager.application.usecases;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.TaskRepository;

public class CreateTaskUseCase {

    private final TaskRepository taskRepository;

    public CreateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }
}
