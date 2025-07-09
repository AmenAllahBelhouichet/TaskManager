package com.example.taskmanger.repository;

import com.example.taskmanger.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
} 