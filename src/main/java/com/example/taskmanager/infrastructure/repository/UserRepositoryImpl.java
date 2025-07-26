package com.example.taskmanager.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.UserRepository;
import com.example.taskmanager.infrastructure.entity.UserEntity;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity savedEntity = jpaUserRepository.save(toEntity(user));
        return toDomain(savedEntity);
    }
    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id).map(this::toDomain);
    }

    private User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPassword()
        );
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword()
        );
    }
}
