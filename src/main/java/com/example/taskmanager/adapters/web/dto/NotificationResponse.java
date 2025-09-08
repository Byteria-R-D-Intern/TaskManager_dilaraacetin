package com.example.taskmanager.adapters.web.dto;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private Long userId;
    private Long actorUserId;   
    private Long targetUserId;  
    private String type;
    private String title;
    private String body;        
    private String priority;
    private boolean read;
    private LocalDateTime readAt;      
    private Long sourceLogId;          
    private LocalDateTime createdAt;

    public NotificationResponse(Long id, Long userId, Long actorUserId, Long targetUserId,
                                String type, String title, String body, String priority,
                                boolean read, LocalDateTime readAt, Long sourceLogId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.actorUserId = actorUserId;
        this.targetUserId = targetUserId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.priority = priority;
        this.read = read;
        this.readAt = readAt;
        this.sourceLogId = sourceLogId;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getActorUserId() { return actorUserId; }
    public Long getTargetUserId() { return targetUserId; }
    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getPriority() { return priority; }
    public boolean isRead() { return read; }
    public LocalDateTime getReadAt() { return readAt; }
    public Long getSourceLogId() { return sourceLogId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
