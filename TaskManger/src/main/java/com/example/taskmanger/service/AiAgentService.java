package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// Add import for registry and handler
import com.example.taskmanger.service.AiAgentRegistry;
import com.example.taskmanger.service.AIAgentHandler;

@Service
public class AiAgentService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskColumnService taskColumnService;
    @Autowired
    private UserService userService;
    @Autowired
    private AiAgentRegistry aiAgentRegistry;

    // Executes a prompt for an AI agent (now just routes to the handler)
    public String executePrompt(String intent, String targetEntity) {
        String result;
        if (intent != null) {
            AIAgentHandler handler = aiAgentRegistry.getHandlerForAction(intent);
            if (handler != null) {
                result = handler.handle(targetEntity);
            } else {
                result = "No handler found for intent: " + intent;
            }
        } else {
            result = "No intent provided";
        }
        return result;
    }
} 