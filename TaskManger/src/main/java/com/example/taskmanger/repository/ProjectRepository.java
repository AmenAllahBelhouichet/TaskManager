package com.example.taskmanger.repository;

import com.example.taskmanger.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByOwnerId(int ownerId);
} 