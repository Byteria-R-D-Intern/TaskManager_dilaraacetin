package com.example.taskmanager.adapters.web.dto;

import java.time.LocalDateTime;

import com.example.taskmanager.domain.model.Notification;

public class NotificationResponse {

    private Long id;
    private Long actorUserId;
    private String actorUsername;  
    private Long targetUserId;
    private String targetUsername;  
    private String type;
    private String title;
    private String body;
    private String priority;
    private boolean read;
    private LocalDateTime readAt;
    private Long sourceLogId;
    private LocalDateTime createdAt;

    public NotificationResponse() {}

    public NotificationResponse(Long id,
                                Long actorUserId,
                                String actorUsername,
                                Long targetUserId,
                                String targetUsername,
                                String type,
                                String title,
                                String body,
                                String priority,
                                boolean read,
                                LocalDateTime readAt,
                                Long sourceLogId,
                                LocalDateTime createdAt) {
        this.id = id;
        this.actorUserId = actorUserId;
        this.actorUsername = actorUsername;
        this.targetUserId = targetUserId;
        this.targetUsername = targetUsername;
        this.type = type;
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.read = read;
        this.readAt = readAt;
        this.sourceLogId = sourceLogId;
        this.createdAt = createdAt;
    }

    public static NotificationResponse of(Notification n, String actorUsername, String targetUsername) {
        return new NotificationResponse(
            n.getId(),
            n.getActorUserId(),
            actorUsername,
            n.getTargetUserId(),
            targetUsername,
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

    public Long getId() { return id; }
    public Long getActorUserId() { return actorUserId; }
    public String getActorUsername() { return actorUsername; }
    public Long getTargetUserId() { return targetUserId; }
    public String getTargetUsername() { return targetUsername; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getPriority() { return priority; }
    public boolean isRead() { return read; }
    public LocalDateTime getReadAt() { return readAt; }
    public Long getSourceLogId() { return sourceLogId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setActorUserId(Long actorUserId) { this.actorUserId = actorUserId; }
    public void setActorUsername(String actorUsername) { this.actorUsername = actorUsername; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public void setTargetUsername(String targetUsername) { this.targetUsername = targetUsername; }
    public void setType(String type) { this.type = type; }
    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setRead(boolean read) { this.read = read; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    public void setSourceLogId(Long sourceLogId) { this.sourceLogId = sourceLogId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
