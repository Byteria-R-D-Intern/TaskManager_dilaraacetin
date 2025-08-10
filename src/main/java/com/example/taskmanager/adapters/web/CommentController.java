package com.example.taskmanager.adapters.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.adapters.web.dto.CommentRequest;
import com.example.taskmanager.adapters.web.dto.CommentResponse;
import com.example.taskmanager.application.usecases.CommentService;
import com.example.taskmanager.domain.model.Comment;
import com.example.taskmanager.domain.model.User;
import com.example.taskmanager.domain.ports.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository; // <— eklendi

    public CommentController(CommentService commentService, UserRepository userRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long taskId,
                                                      @Valid @RequestBody CommentRequest request,
                                                      Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        Comment comment = commentService.addComment(taskId, userId, request.getContent());

        String username = userRepository.findById(comment.getUserId())
                .map(User::getUsername)
                .orElse("Unknown");

        CommentResponse response = new CommentResponse(
            comment.getId(),
            comment.getUserId(),
            username,                // <—
            comment.getContent(),
            comment.getTimestamp()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long taskId) {
        List<Comment> comments = commentService.getCommentsForTask(taskId);

        List<CommentResponse> responses = comments.stream()
            .map(c -> new CommentResponse(
                c.getId(),
                c.getUserId(),
                userRepository.findById(c.getUserId()).map(User::getUsername).orElse("Unknown"), // <—
                c.getContent(),
                c.getTimestamp()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
