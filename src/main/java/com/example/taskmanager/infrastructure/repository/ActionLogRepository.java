package com.example.taskmanager.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanager.infrastructure.entity.ActionLogEntity;

public interface ActionLogRepository extends JpaRepository<ActionLogEntity, Long> {

    List<ActionLogEntity> findByActorUserIdOrderByTimestampDesc(Long actorUserId);

    List<ActionLogEntity> findByTargetUserIdOrderByTimestampDesc(Long targetUserId);

    List<ActionLogEntity> findByActorUserIdOrTargetUserIdOrderByTimestampDesc(Long actorUserId, Long targetUserId);
}
