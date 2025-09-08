package com.example.taskmanager.infrastructure.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity @Table(name = "notifications")
public class NotificationEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false) private Long userId;
    @Column(name="actor_user_id") private Long actorUserId;
    @Column(name="target_user_id") private Long targetUserId;

    @Column(nullable=false, length=64) private String type;
    @Column(nullable=false, length=160) private String title;
    @Column(columnDefinition="TEXT") private String body;
    @Column(nullable=false, length=16) private String priority;

    @Column(name="is_read", nullable=false) private boolean read;
    @Column(name="read_at") private LocalDateTime readAt;

    @Column(name="source_log_id") private Long sourceLogId;
    @Column(name="created_at", nullable=false) private LocalDateTime createdAt;

    @PrePersist void prePersist(){ if(createdAt==null) createdAt = LocalDateTime.now(); }


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
