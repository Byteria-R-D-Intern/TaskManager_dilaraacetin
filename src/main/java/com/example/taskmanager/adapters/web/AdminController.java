package com.example.taskmanager.adapters.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.UpdateRoleRequest;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.domain.model.RoleValidator;
import com.example.taskmanager.domain.model.User;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final RegisterUserUseCase registerUserUseCase;
    private final ActionLogService actionLogService;

    public AdminController(RegisterUserUseCase registerUserUseCase,
                           ActionLogService actionLogService) {
        this.registerUserUseCase = registerUserUseCase;
        this.actionLogService = actionLogService;
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id,
                                            @Valid @RequestBody UpdateRoleRequest request,
                                            Authentication authentication) {
        if (!RoleValidator.isValid(request.getRole())) {
            return ResponseEntity.badRequest().body("Invalid role: " + request.getRole());
        }

        User updated = registerUserUseCase.updateUserRole(id, request.getRole());
        Long adminId = (Long) authentication.getPrincipal();
        actionLogService.log(adminId, "UPDATE_ROLE", "User", id);

        return ResponseEntity.ok(updated);
    }
}
