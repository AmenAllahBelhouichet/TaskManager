package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignTaskAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    public String getSupportedAction() {
        return "assign_task";
    }

    @Override
    public String handle(String targetEntity) {
        try {
            // Format: assign_task:taskId:userId
            String[] parts = targetEntity.split(":");
            int taskId = Integer.parseInt(parts[1]);
            
            int userId = Integer.parseInt(parts[2]);
            Task task = taskService.getTaskById(taskId).orElseThrow();
            task.setAssignTo(userService.getUserById(userId).orElse(null));
            taskService.updateTask(taskId, task);
            return "Task assigned";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 