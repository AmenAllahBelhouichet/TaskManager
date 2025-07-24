package com.example.taskmanger.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class AiAgentRegistry {
    private final Map<String, AIAgentHandler> handlerMap = new HashMap<>();

    @Autowired
    public AiAgentRegistry(ApplicationContext context) {
        Map<String, AIAgentHandler> beans = context.getBeansOfType(AIAgentHandler.class);
        for (AIAgentHandler handler : beans.values()) {
            handlerMap.put(handler.getSupportedAction(), handler);
        }
    }

    public AIAgentHandler getHandlerForAction(String action) {
        return handlerMap.get(action);
    }
} 