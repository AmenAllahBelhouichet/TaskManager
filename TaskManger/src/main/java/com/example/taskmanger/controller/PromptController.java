package com.example.taskmanger.controller;

import com.example.taskmanger.model.Prompt;
import com.example.taskmanger.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prompts")
public class PromptController {
    @Autowired
    private PromptService promptService;

    @GetMapping
    public List<Prompt> getAllPrompts() {
        return promptService.getAllPrompts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prompt> getPromptById(@PathVariable int id) {
        return promptService.getPromptById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Prompt createPrompt(@RequestBody Prompt prompt) {
        return promptService.createPrompt(prompt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prompt> updatePrompt(@PathVariable int id, @RequestBody Prompt prompt) {
        if (!promptService.getPromptById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(promptService.updatePrompt(id, prompt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(@PathVariable int id) {
        if (!promptService.getPromptById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        promptService.deletePrompt(id);
        return ResponseEntity.noContent().build();
    }
} 