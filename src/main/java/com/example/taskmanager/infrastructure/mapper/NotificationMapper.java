package com.example.taskmanager.infrastructure.mapper;

import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.infrastructure.entity.NotificationEntity;

public class NotificationMapper {

    public static NotificationEntity toEntity(Notification n) {
        if (n == null) return null;
        return new NotificationEntity(
            n.getId(),
            n.getActorUserId(),
            n.getTargetUserId(),
            n.getType(),
            n.getTitle(),
            n.getBody(),
            n.getPriority(),
            n.isRead(),
            n.getReadAt(),
            n.getSourceLogId(),
            n.getCreatedAt()
        );
    }

    public static Notification toDomain(NotificationEntity e) {
        if (e == null) return null;
        return new Notification(
            e.getId(),
            e.getActorUserId(),
            e.getTargetUserId(),
            e.getType(),
            e.getTitle(),
            e.getBody(),
            e.getPriority(),
            e.isRead(),
            e.getReadAt(),
            e.getSourceLogId(),
            e.getCreatedAt()
        );
    }
}
