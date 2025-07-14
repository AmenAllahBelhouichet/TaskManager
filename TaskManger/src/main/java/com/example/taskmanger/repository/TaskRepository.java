package com.example.taskmanger.repository;

import com.example.taskmanger.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByTaskColumn_Id(int columnId);
} 