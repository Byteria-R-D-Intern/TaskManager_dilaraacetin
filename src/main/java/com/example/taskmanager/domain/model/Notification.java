package com.example.taskmanager.domain.model;

import java.time.LocalDateTime;

public class Notification {
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

    public Notification() {}
    public Notification(Long id, Long userId, Long actorUserId, Long targetUserId,
                        String type, String title, String body, String priority,
                        boolean read, LocalDateTime readAt, Long sourceLogId, LocalDateTime createdAt) {
        this.id = id; this.userId = userId; this.actorUserId = actorUserId; this.targetUserId = targetUserId;
        this.type = type; this.title = title; this.body = body; this.priority = priority;
        this.read = read; this.readAt = readAt; this.sourceLogId = sourceLogId; this.createdAt = createdAt;
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

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setActorUserId(Long actorUserId) { this.actorUserId = actorUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public void setType(String type) { this.type = type; }
    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setRead(boolean read) { this.read = read; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    public void setSourceLogId(Long sourceLogId) { this.sourceLogId = sourceLogId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
