package com.example.taskmanager.adapters.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.LoginRequest;
import com.example.taskmanager.adapters.web.dto.LoginResponse;
import com.example.taskmanager.adapters.web.dto.UserRequest;
import com.example.taskmanager.application.usecases.LoginUserUseCase;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.domain.model.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest) {
        User user = new User(
            null,
            userRequest.getUsername(),
            userRequest.getEmail(),
            userRequest.getPassword(),
            "ROLE_USER"
        );
        User registeredUser = registerUserUseCase.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = loginUserUseCase.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
