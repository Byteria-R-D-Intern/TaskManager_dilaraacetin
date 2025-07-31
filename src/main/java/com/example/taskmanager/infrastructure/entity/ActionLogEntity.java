package com.example.taskmanager.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
public class ActionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String action;
    private String resource;
    private Long resourceId;
    private LocalDateTime timestamp;

    public ActionLogEntity() {}

    public ActionLogEntity(Long userId, String action, String resource, Long resourceId, LocalDateTime timestamp) {
        this.userId = userId;
        this.action = action;
        this.resource = resource;
        this.resourceId = resourceId;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }
    public Long getResourceId() { return resourceId; }
    public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}