package com.example.taskmanger.service;

import com.example.taskmanger.model.Prompt;
import com.example.taskmanger.repository.PromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromptService {
    @Autowired
    private PromptRepository promptRepository;

    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }

    public Optional<Prompt> getPromptById(int id) {
        return promptRepository.findById(id);
    }

    public Prompt createPrompt(Prompt prompt) {
        return promptRepository.save(prompt);
    }

    public Prompt updatePrompt(int id, Prompt prompt) {
        prompt.setId(id);
        return promptRepository.save(prompt);
    }

    public void deletePrompt(int id) {
        promptRepository.deleteById(id);
    }
} 