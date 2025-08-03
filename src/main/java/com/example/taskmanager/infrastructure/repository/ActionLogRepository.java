package com.example.taskmanager.infrastructure.repository;

import com.example.taskmanager.infrastructure.entity.ActionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {
    List<ActionLogEntity> findByUserId(Long userId);
}
