package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import java.util.List;

@Component
public class TaskDeleteAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;

    @Override
    public String getSupportedAction() {
        return "delete_task";
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
            taskService.deleteTask(task.getId());
            return "Task deleted";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 