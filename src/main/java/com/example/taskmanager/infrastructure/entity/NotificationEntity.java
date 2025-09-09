package com.example.taskmanager.infrastructure.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "notifications",
    indexes = {
        @Index(name = "ix_ntf_actor_ts",  columnList = "actor_user_id,created_at"),
        @Index(name = "ix_ntf_target_ts", columnList = "target_user_id,created_at"),
        @Index(name = "fk_ntf_log",       columnList = "source_log_id")
    }
)
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_user_id")
    private Long actorUserId;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Column(name = "type", nullable = false, length = 64)
    private String type;

    @Column(name = "title", nullable = false, length = 160)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "priority", nullable = false, length = 16)
    private String priority;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "source_log_id")
    private Long sourceLogId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public NotificationEntity() { }

    public NotificationEntity(Long id,
                              Long actorUserId,
                              Long targetUserId,
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

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
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
