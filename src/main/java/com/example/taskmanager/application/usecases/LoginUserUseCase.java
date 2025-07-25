package com.example.taskmanager.application.usecases;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.taskmanager.config.JwtUtil;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.UserRepository;

public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginUserUseCase(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getId());
    }
}
