package com.example.taskmanager.domain.ports;

import java.util.Optional;

import com.example.taskmanager.domain.model.Task;

public interface DeleteTaskUseCase {
    Optional<Task> getById(Long id);
    void delete(Long id);
}
