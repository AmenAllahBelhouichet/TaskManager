package com.example.taskmanger.service;

import com.example.taskmanger.model.Comments;
import com.example.taskmanger.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {
    @Autowired
    private CommentsRepository commentsRepository;

    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    public Optional<Comments> getCommentById(int id) {
        return commentsRepository.findById(id);
    }

    public Comments createComment(Comments comment) {
        return commentsRepository.save(comment);
    }

    public Comments updateComment(int id, Comments comment) {
        comment.setId(id);
        return commentsRepository.save(comment);
    }

    public void deleteComment(int id) {
        commentsRepository.deleteById(id);
    }
} 