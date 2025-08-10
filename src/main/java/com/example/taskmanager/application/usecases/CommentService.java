package com.example.taskmanager.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.domain.exception.ForbiddenException;
import com.example.taskmanager.domain.exception.NotFoundException;
import com.example.taskmanager.domain.model.Comment;
import com.example.taskmanager.domain.ports.CommentRepository;
import com.example.taskmanager.domain.ports.TaskRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository,
                          TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    public Comment addComment(Long taskId, Long userId, String content) {
        var taskOpt = taskRepository.findById(taskId);

        if (taskOpt.isEmpty()) {
            throw new NotFoundException("Task with ID " + taskId + " not found");
        }

        var task = taskOpt.get();
        if (!task.getUserId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to comment on this task");
        }

        Comment comment = new Comment(null, taskId, userId, content, LocalDateTime.now());
        return commentRepository.save(comment);
    }


    public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
    
}
