package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import java.util.List;

@Component
public class TaskAssignAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    public String getSupportedAction() {
        return "assign_task_only";
    }

    @Override
    public String handle(String targetEntity) {
        try {
            JSONObject json = new JSONObject(targetEntity);
            Task task = null;
            // Find by ID or title
            if (json.has("id")) {
                int id = json.getInt("id");
                task = taskService.getTaskById(id).orElse(null);
            } else if (json.has("title")) {
                List<Task> tasks = taskService.getAllTasks();
                String title = json.getString("title");
                task = tasks.stream().filter(t -> t.getTitle() != null && t.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
            }
            if (task == null) return "Task not found";
            if (!json.has("assignTo")) return "No user specified to assign";
            String email = json.getString("assignTo");
            User user = userService.findByEmail(email).orElse(null);
            if (user == null) return "User not found";
            task.setAssignTo(user);
            taskService.updateTask(task.getId(), task);
            return "Task assigned to user";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 