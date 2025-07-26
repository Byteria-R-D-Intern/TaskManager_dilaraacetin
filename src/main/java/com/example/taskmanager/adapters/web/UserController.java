package com.example.taskmanager.adapters.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.LoginRequest;
import com.example.taskmanager.adapters.web.dto.LoginResponse;
import com.example.taskmanager.application.usecases.LoginUserUseCase;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.domain.model.User;

@RestController
@RequestMapping("/api")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = registerUserUseCase.register(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = loginUserUseCase.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = registerUserUseCase.getUserById(id);

        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}
