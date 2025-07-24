package com.example.taskmanager.application.usecases;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.UserRepository;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
}
