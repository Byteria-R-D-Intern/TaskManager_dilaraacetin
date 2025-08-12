package com.example.taskmanager.application.usecases;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmanager.domain.exception.ConflictException;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.CommentRepository;
import com.example.taskmanager.domain.ports.TaskRepository;
import com.example.taskmanager.domain.ports.UserRepository;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public RegisterUserUseCase(UserRepository userRepository,
                               BCryptPasswordEncoder passwordEncoder,
                               TaskRepository taskRepository,
                               CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
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
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            if (!user.getPassword().startsWith("$2a$") &&
                !user.getPassword().startsWith("$2b$") &&
                !user.getPassword().startsWith("$2y$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserId(userId);

        for (Task t : tasks) {
            commentRepository.deleteByTaskId(t.getId());
            taskRepository.deleteById(t.getId());
        }

        userRepository.deleteById(userId);
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
