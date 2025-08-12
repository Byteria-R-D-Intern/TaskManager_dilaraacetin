package com.example.taskmanager.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.taskmanager.domain.model.Comment;
import com.example.taskmanager.domain.ports.CommentRepository;
import com.example.taskmanager.infrastructure.entity.CommentEntity;
import com.example.taskmanager.infrastructure.mapper.CommentMapper;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository jpaCommentRepository;

    public CommentRepositoryImpl(JpaCommentRepository jpaCommentRepository) {
        this.jpaCommentRepository = jpaCommentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = CommentMapper.toEntity(comment);
        CommentEntity saved = jpaCommentRepository.save(entity);
        return CommentMapper.toDomain(saved);
    }

    @Override
    public List<Comment> findByTaskId(Long taskId) {
        return jpaCommentRepository.findByTaskId(taskId)
                .stream()
                .map(CommentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByTaskId(Long taskId) {
        jpaCommentRepository.deleteByTaskId(taskId);
    }

    @Override
    public void deleteByTaskIdIn(List<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) return;
        jpaCommentRepository.deleteByTaskIdIn(taskIds);
    }
}
