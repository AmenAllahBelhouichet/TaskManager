package com.example.taskmanger.controller;

import com.example.taskmanger.model.Comments;
import com.example.taskmanger.service.CommentsService;
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
    public Comments createComment(@RequestBody Comments comment) {
        return commentsService.createComment(comment);
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