package com.example.taskmanager.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.TaskRepository;
import com.example.taskmanager.infrastructure.entity.TaskEntity;
import com.example.taskmanager.infrastructure.mapper.TaskMapper;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JpaTaskRepository jpaTaskRepository;
    private final TaskMapper taskMapper;

    public TaskRepositoryImpl(JpaTaskRepository jpaTaskRepository, TaskMapper taskMapper) {
        this.jpaTaskRepository = jpaTaskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        TaskEntity saved = jpaTaskRepository.save(entity);
        return taskMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaTaskRepository.findById(id).map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return jpaTaskRepository.findAllByUserId(userId)
                .stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findAll() {
        return jpaTaskRepository.findAll()
                .stream()
                .map(taskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaTaskRepository.deleteById(id);
    }
}
