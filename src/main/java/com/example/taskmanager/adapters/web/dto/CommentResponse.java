package com.example.taskmanager.adapters.web.dto;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public CommentResponse() {}

    public CommentResponse(Long id, Long userId, String username, String content, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

     public String getUsername() { 
        return username; 
    }
}
