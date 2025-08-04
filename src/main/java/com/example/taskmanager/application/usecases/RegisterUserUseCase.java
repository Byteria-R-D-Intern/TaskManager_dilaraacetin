package com.example.taskmanager.application.usecases;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.taskmanager.domain.exception.ConflictException;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.UserRepository;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public RegisterUserUseCase(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Email is already in use");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User updateUserRole(Long userId, String newRole) {
    Optional<User> userOpt = userRepository.findById(userId);

    if (userOpt.isEmpty()) {
        throw new IllegalArgumentException("User not found");
    }

    User user = userOpt.get();
    user.setRole(newRole);

    return userRepository.save(user);
}


}
