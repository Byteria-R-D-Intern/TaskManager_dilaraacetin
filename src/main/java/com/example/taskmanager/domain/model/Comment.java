package com.example.taskmanager.domain.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Long taskId;
    private Long userId;
    private String content;
    private LocalDateTime timestamp;

    public Comment() {}

    public Comment(Long id, Long taskId, Long userId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.taskId = taskId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
