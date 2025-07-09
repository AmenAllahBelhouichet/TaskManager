package com.example.taskmanger.repository;

import com.example.taskmanger.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Integer> {
} 