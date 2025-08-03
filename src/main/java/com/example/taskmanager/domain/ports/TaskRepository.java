package com.example.taskmanager.domain.ports;

import java.util.List;
import java.util.Optional;

import com.example.taskmanager.domain.model.Task;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
    List<Task> findAll(); // <-- eklendi
    void deleteById(Long id);
}
