package com.example.taskmanager.adapters.web.dto;

import java.time.LocalDateTime;

public class ActionLogResponse {

    private Long id;
    private Long userId;
    private String action;
    private String resource;
    private Long resourceId;
    private LocalDateTime timestamp;

    public ActionLogResponse(Long id, Long userId, String action, String resource, Long resourceId, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.resource = resource;
        this.resourceId = resourceId;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public Long getResourceId() { return resourceId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
