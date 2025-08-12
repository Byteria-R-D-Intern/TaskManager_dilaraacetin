package com.example.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.taskmanager.application.usecases.CreateTaskUseCase;
import com.example.taskmanager.application.usecases.GetTasksByUserUseCase;
import com.example.taskmanager.application.usecases.LoginUserUseCase;
import com.example.taskmanager.application.usecases.RegisterUserUseCase;
import com.example.taskmanager.application.usecases.UpdateTaskUseCase;
import com.example.taskmanager.config.JwtUtil;
import com.example.taskmanager.domain.ports.CommentRepository;
import com.example.taskmanager.domain.ports.TaskRepository;
import com.example.taskmanager.domain.ports.UserRepository;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class TaskmanagerApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                              .ignoreIfMissing()
                              .load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(TaskmanagerApplication.class, args);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                   BCryptPasswordEncoder passwordEncoder,
                                                   TaskRepository taskRepository,
                                                   CommentRepository commentRepository) {
        return new RegisterUserUseCase(userRepository, passwordEncoder, taskRepository, commentRepository);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(UserRepository userRepository,
                                             BCryptPasswordEncoder passwordEncoder,
                                             JwtUtil jwtUtil) {
        return new LoginUserUseCase(userRepository, passwordEncoder, jwtUtil);
    }

    @Bean
    public CreateTaskUseCase createTaskUseCase(TaskRepository taskRepository) {
        return new CreateTaskUseCase(taskRepository);
    }

    @Bean
    public GetTasksByUserUseCase getTasksByUserUseCase(TaskRepository taskRepository) {
        return new GetTasksByUserUseCase(taskRepository);
    }

    @Bean
    public UpdateTaskUseCase updateTaskUseCase(TaskRepository taskRepository) {
        return new UpdateTaskUseCase(taskRepository);
    }
}
