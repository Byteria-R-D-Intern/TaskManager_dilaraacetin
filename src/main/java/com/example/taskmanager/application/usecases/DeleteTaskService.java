package com.example.taskmanager.application.usecases;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.ports.CommentRepository;
import com.example.taskmanager.domain.ports.DeleteTaskUseCase;
import com.example.taskmanager.domain.ports.TaskRepository;

@Service
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public DeleteTaskService(TaskRepository taskRepository,
                             CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        // Önce task’e bağlı yorumları sil
        commentRepository.deleteByTaskId(id);
        // Sonra task’i sil
        taskRepository.deleteById(id);
    }
}
