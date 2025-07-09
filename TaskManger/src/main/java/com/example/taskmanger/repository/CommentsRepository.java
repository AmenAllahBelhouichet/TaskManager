package com.example.taskmanger.repository;

import com.example.taskmanger.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
} 