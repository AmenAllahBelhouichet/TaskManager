package com.example.taskmanger.repository;

import com.example.taskmanger.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
} 