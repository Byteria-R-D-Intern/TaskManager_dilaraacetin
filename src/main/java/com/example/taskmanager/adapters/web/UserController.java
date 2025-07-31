package com.example.taskmanager.adapters.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.LoginRequest;
import com.example.taskmanager.adapters.web.dto.LoginResponse;
import com.example.taskmanager.adapters.web.dto.UserRequest;
import com.example.taskmanager.adapters.web.dto.UserUpdateRequest;
import com.example.taskmanager.application.usecases.ActionLogService;
import com.example.taskmanager.application.usecases.LoginUserUseCase;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final ActionLogService actionLogService;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          ActionLogService actionLogService) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.actionLogService = actionLogService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        User user = new User(null, userRequest.getUsername(), userRequest.getEmail(), userRequest.getPassword());
        User registeredUser = registerUserUseCase.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = loginUserUseCase.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
        Optional<User> userOpt = registerUserUseCase.getUserById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Long userIdFromToken = (Long) authentication.getPrincipal();

        if (!userIdFromToken.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(userOpt.get());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @Valid @RequestBody UserUpdateRequest request,
                                        Authentication authentication) {
        Optional<User> existingUserOpt = registerUserUseCase.getUserById(id);

        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Long userIdFromToken = (Long) authentication.getPrincipal();

        if (!userIdFromToken.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        User existingUser = existingUserOpt.get();
        existingUser.setUsername(request.getUsername());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());

        User savedUser = registerUserUseCase.updateUser(existingUser);

        // ✅ Log the update action
        actionLogService.log(userIdFromToken, "UPDATE", "User", savedUser.getId());

        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication) {
        Optional<User> userOpt = registerUserUseCase.getUserById(id);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Long userIdFromToken = (Long) authentication.getPrincipal();

        if (!userIdFromToken.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        registerUserUseCase.deleteUser(id);

        // ✅ Log the delete action
        actionLogService.log(userIdFromToken, "DELETE", "User", id);

        return ResponseEntity.noContent().build();
    }
}
