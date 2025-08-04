package com.example.taskmanager.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.adapters.web.dto.TaskStatisticsResponse;
import com.example.taskmanager.domain.model.Task;
import com.example.taskmanager.domain.model.TaskStatus;
import com.example.taskmanager.domain.ports.TaskRepository;

@Service
public class TaskStatsService {

    private final TaskRepository taskRepository;

    public TaskStatsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskStatisticsResponse getStatsForUser(Long userId) {
        List<Task> tasks = taskRepository.findAllByUserId(userId);

        long total = tasks.size();
        long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        return new TaskStatisticsResponse(total, todo, inProgress, done);
    }

    public TaskStatisticsResponse getStatsForAll() {
        List<Task> tasks = taskRepository.findAll();

        long total = tasks.size();
        long todo = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgress = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long done = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();

        return new TaskStatisticsResponse(total, todo, inProgress, done);
    }
}
