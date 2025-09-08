package com.example.taskmanager.infrastructure.mapper;

import com.example.taskmanager.domain.model.Notification;
import com.example.taskmanager.infrastructure.entity.NotificationEntity;

public class NotificationMapper {

    public static NotificationEntity toEntity(Notification n) {   
        NotificationEntity e = new NotificationEntity();
        e.setId(n.getId());
        e.setUserId(n.getUserId());
        e.setActorUserId(n.getActorUserId());
        e.setTargetUserId(n.getTargetUserId());
        e.setType(n.getType());
        e.setTitle(n.getTitle());
        e.setBody(n.getBody());
        e.setPriority(n.getPriority());
        e.setRead(n.isRead());
        e.setReadAt(n.getReadAt());
        e.setSourceLogId(n.getSourceLogId());
        e.setCreatedAt(n.getCreatedAt());
        return e;
    }

    public static Notification toDomain(NotificationEntity e) {
        return new Notification(
            e.getId(),
            e.getUserId(),
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
