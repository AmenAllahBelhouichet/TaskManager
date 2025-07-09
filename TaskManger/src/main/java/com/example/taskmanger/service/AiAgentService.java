package com.example.taskmanger.service;

import com.example.taskmanger.model.AiAgent;
import com.example.taskmanger.repository.AiAgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AiAgentService {
    @Autowired
    private AiAgentRepository aiAgentRepository;

    public List<AiAgent> getAllAiAgents() {
        return aiAgentRepository.findAll();
    }

    public Optional<AiAgent> getAiAgentById(int id) {
        return aiAgentRepository.findById(id);
    }

    public AiAgent createAiAgent(AiAgent aiAgent) {
        return aiAgentRepository.save(aiAgent);
    }

    public AiAgent updateAiAgent(int id, AiAgent aiAgent) {
        aiAgent.setId(id);
        return aiAgentRepository.save(aiAgent);
    }

    public void deleteAiAgent(int id) {
        aiAgentRepository.deleteById(id);
    }
} 