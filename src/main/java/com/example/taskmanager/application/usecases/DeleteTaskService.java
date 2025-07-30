package com.example.taskmanager.application.usecases;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.DeleteTaskUseCase;
import com.example.taskmanager.domain.ports.TaskRepository;

@Service
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepository taskRepository;

    public DeleteTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
