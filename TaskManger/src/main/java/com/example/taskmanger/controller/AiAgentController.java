package com.example.taskmanger.controller;

import com.example.taskmanger.model.AiAgent;
import com.example.taskmanger.service.AiAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai-agents")
public class AiAgentController {
    @Autowired
    private AiAgentService aiAgentService;

    @GetMapping
    public List<AiAgent> getAllAiAgents() {
        return aiAgentService.getAllAiAgents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiAgent> getAiAgentById(@PathVariable int id) {
        return aiAgentService.getAiAgentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AiAgent createAiAgent(@RequestBody AiAgent aiAgent) {
        return aiAgentService.createAiAgent(aiAgent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiAgent> updateAiAgent(@PathVariable int id, @RequestBody AiAgent aiAgent) {
        if (!aiAgentService.getAiAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aiAgentService.updateAiAgent(id, aiAgent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAiAgent(@PathVariable int id) {
        if (!aiAgentService.getAiAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        aiAgentService.deleteAiAgent(id);
        return ResponseEntity.noContent().build();
    }
} 