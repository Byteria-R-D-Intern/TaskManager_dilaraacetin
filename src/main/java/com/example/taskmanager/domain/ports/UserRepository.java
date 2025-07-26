package com.example.taskmanager.domain.ports;

import java.util.Optional;

import com.example.taskmanager.domain.model.User;

public interface UserRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    User save(User user);
}
