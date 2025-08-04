package com.example.taskmanager.domain.model;

import java.util.Set;

public class RoleValidator {
    
    private static final Set<String> VALID_ROLES = Set.of(
        "ROLE_USER",
        "ROLE_MANAGER",
        "ROLE_ADMIN"
    );

    public static boolean isValid(String role) {
        return VALID_ROLES.contains(role);
    }

    public static Set<String> getValidRoles() {
        return VALID_ROLES;
    }
}
