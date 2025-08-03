package com.example.taskmanager.adapters.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.UserUpdateRequest;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final ActionLogService actionLogService;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          ActionLogService actionLogService) {
        this.registerUserUseCase = registerUserUseCase;
        this.actionLogService = actionLogService;
    }

    @GetMapping
    public ResponseEntity<?> getUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        return registerUserUseCase.getUserById(userId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest request,
                                        Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        Optional<User> existingUserOpt = registerUserUseCase.getUserById(userId);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User existingUser = existingUserOpt.get();
        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());

        User savedUser = registerUserUseCase.updateUser(existingUser);
        actionLogService.log(userId, "UPDATE", "User", savedUser.getId());

        return ResponseEntity.ok(savedUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        Optional<User> userOpt = registerUserUseCase.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        registerUserUseCase.deleteUser(userId);
        actionLogService.log(userId, "DELETE", "User", userId);

        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<User> userOpt = registerUserUseCase.getUserById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        registerUserUseCase.deleteUser(id);
        actionLogService.log(
            (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
            "DELETE",
            "User",
            id
        );

        return ResponseEntity.noContent().build();
    }



}
