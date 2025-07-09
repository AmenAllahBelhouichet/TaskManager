package com.example.taskmanger.repository;

import com.example.taskmanger.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
} 