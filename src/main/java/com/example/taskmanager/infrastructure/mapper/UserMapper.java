package com.example.taskmanager.infrastructure.mapper;

import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.infrastructure.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        return new UserEntity(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getRole()
        );
    }

    public static User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole()
        );
    }
}
