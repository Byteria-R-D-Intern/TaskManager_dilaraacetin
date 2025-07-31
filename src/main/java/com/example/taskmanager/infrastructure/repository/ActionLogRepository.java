package com.example.taskmanager.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanager.infrastructure.entity.ActionLogEntity;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
    
    List<ActionLogEntity> findByUserId(Long userId);
}
