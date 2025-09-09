package com.example.taskmanager.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.taskmanager.infrastructure.entity.NotificationEntity;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Optional<NotificationEntity> findByIdAndTargetUserId(Long id, Long targetUserId);

    List<NotificationEntity> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update NotificationEntity n
           set n.read = true, n.readAt = :readAt
         where n.targetUserId = :targetUserId and n.read = false
    """)
    int markAllAsReadByTargetUserId(@Param("targetUserId") Long targetUserId,
                                    @Param("readAt") LocalDateTime readAt);
}
