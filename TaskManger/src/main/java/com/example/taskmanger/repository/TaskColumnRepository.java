package com.example.taskmanger.repository;

import com.example.taskmanger.model.TaskColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskColumnRepository extends JpaRepository<TaskColumn, Integer> {
    List<TaskColumn> findByBoardId(int boardId);
} 