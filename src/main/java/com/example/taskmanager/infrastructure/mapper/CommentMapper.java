package com.example.taskmanager.infrastructure.mapper;

import com.example.taskmanager.domain.model.Comment;
import com.example.taskmanager.infrastructure.entity.CommentEntity;

public class CommentMapper {

    public static CommentEntity toEntity(Comment comment) {
        return new CommentEntity(
            comment.getId(),
            comment.getTaskId(),
            comment.getUserId(),
            comment.getContent(),
            comment.getTimestamp()
        );
    }

    public static Comment toDomain(CommentEntity entity) {
        return new Comment(
            entity.getId(),
            entity.getTaskId(),
            entity.getUserId(),
            entity.getContent(),
            entity.getTimestamp()
        );
    }
}
