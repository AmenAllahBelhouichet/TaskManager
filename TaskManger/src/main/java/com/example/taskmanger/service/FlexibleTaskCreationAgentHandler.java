package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class FlexibleTaskCreationAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskColumnService taskColumnService;
    @Autowired
    private UserService userService;

    @Override
    public String getSupportedAction() {
        return "flexible_create_task";
    }

    @Override
    public String handle(String targetEntity) {
        try {
            JSONObject json = new JSONObject(targetEntity);
            Task task = new Task();
            // Title
            if (json.has("title")) task.setTitle(json.getString("title"));
            // Description
            if (json.has("description")) task.setDescription(json.getString("description"));
            // Status
            if (json.has("status")) task.setStatus(json.getString("status"));
            // Deadline
            if (json.has("deadline")) {
                try {
                    String deadlineStr = json.getString("deadline");
                    Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineStr);
                    task.setDeadline(deadline);
                } catch (Exception e) { /* ignore parse errors, leave null */ }
            }
            // AssignTo
            if (json.has("assignTo")) {
                String email = json.getString("assignTo");
                User user = userService.findByEmail(email).orElse(null);
                task.setAssignTo(user);
            }
            // Column
            if (json.has("column")) {
                String columnName = json.getString("column");
                List<TaskColumn> columns = taskColumnService.getAllTaskColumns();
                TaskColumn column = columns.stream()
                        .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(columnName))
                        .findFirst().orElse(null);
                task.setTaskColumn(column);
            }
            taskService.createTask(task);
            return "Task created";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 