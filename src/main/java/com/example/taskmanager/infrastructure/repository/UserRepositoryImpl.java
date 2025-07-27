package com.example.taskmanager.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.UserRepository;
import com.example.taskmanager.infrastructure.entity.UserEntity;
import com.example.taskmanager.infrastructure.mapper.UserMapper;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = jpaUserRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }
}
