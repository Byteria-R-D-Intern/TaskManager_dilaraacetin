package com.example.taskmanager.application.usecases;

import java.util.Optional;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.TaskRepository;

public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;

    public UpdateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }
}
