package com.example.taskmanager.adapters.web.dto;

import java.time.LocalDateTime;

public class MyActionLogResponse {
    private Long id;
    private Long actorUserId;
    private Long targetUserId;
    private String action;
    private String resource;
    private Long resourceId;
    private LocalDateTime timestamp;

    public MyActionLogResponse(Long id, Long actorUserId, Long targetUserId,
                               String action, String resource, Long resourceId,
                               LocalDateTime timestamp) {
        this.id = id;
        this.actorUserId = actorUserId;
        this.targetUserId = targetUserId;
        this.action = action;
        this.resource = resource;
        this.resourceId = resourceId;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public Long getActorUserId() { return actorUserId; }
    public Long getTargetUserId() { return targetUserId; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public Long getResourceId() { return resourceId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
