package com.example.taskmanager.domain.ports;

import java.util.List;

import com.example.taskmanager.domain.model.Comment;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findByTaskId(Long taskId);
}
