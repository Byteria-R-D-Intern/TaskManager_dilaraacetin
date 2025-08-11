package com.example.taskmanager.adapters.web.dto;

import com.example.taskmanager.domain.model.User;

public class AdminUserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;

    public AdminUserResponse(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public AdminUserResponse(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
