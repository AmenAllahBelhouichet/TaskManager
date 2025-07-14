package com.example.taskmanger.controller;

import com.example.taskmanger.model.Comments;
import com.example.taskmanger.service.CommentsService;
import com.example.taskmanger.repository.UserRepository;
import com.example.taskmanger.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/all")
    public List<Comments> getAllComments() {
        return commentsService.getAllComments();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Comments> getCommentById(@PathVariable int id) {
        return commentsService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> createComment(@RequestBody Comments comment) {
        try {
            System.out.println("Incoming author: " + (comment.getAuthor() != null ? comment.getAuthor().getId() : "null"));
            if (comment.getAuthor() != null && comment.getAuthor().getId() != 0) {
                var user = userRepository.findById(comment.getAuthor().getId()).orElse(null);
                if (user == null) {
                    return ResponseEntity.badRequest().body("Invalid author id");
                }
                comment.setAuthor(user);
            } else {
                return ResponseEntity.badRequest().body("Author is required");
            }
            if (comment.getTask() != null && comment.getTask().getId() != 0) {
                var task = taskRepository.findById(comment.getTask().getId()).orElse(null);
                if (task == null) {
                    return ResponseEntity.badRequest().body("Invalid task id");
                }
                comment.setTask(task);
            } else {
                return ResponseEntity.badRequest().body("Task is required");
            }
            comment.setCreatedDate(new java.util.Date());
            Comments saved = commentsService.createComment(comment);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Exception: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable int id, @RequestBody Comments comment) {
        if (!commentsService.getCommentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentsService.updateComment(id, comment));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable int id) {
        if (!commentsService.getCommentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        commentsService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
} 