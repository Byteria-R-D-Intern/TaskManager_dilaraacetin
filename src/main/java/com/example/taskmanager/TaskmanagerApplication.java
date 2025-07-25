package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.taskmanager.application.usecases.LoginUserUseCase;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.config.JwtUtil;
import com.example.taskmanager.domain.ports.UserRepository;


@SpringBootApplication
public class TaskmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                   BCryptPasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }
    @Bean
    public LoginUserUseCase loginUserUseCase(UserRepository userRepository,
                                            BCryptPasswordEncoder passwordEncoder,
                                            JwtUtil jwtUtil) {
        return new LoginUserUseCase(userRepository, passwordEncoder, jwtUtil);
    }

}
