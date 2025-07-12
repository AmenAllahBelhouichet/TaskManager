package com.example.taskmanger.repository;

import com.example.taskmanger.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByProjectId(int projectId);
} 