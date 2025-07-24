package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskCreationAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskColumnService taskColumnService;
    @Autowired
    private UserService userService;

    @Override
    public String getSupportedAction() {
        return "create_task";
    }

    @Override
    public String handle(String targetEntity) {
        try {
            // Format: create_task:title:columnId:assignToId
            String[] parts = targetEntity.split(":");
            String title = parts[1];
            int columnId = Integer.parseInt(parts[2]);
            int assignToId = Integer.parseInt(parts[3]);
            Task task = new Task();
            task.setTitle(title);
            task.setTaskColumn(taskColumnService.getTaskColumnById(columnId).orElse(null));
            task.setAssignTo(userService.getUserById(assignToId).orElse(null));
            taskService.createTask(task);
            return "Task created";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 