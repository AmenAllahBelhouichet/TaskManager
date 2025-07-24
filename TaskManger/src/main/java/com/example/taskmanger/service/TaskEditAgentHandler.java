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
public class TaskEditAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskColumnService taskColumnService;
    @Autowired
    private UserService userService;

    @Override
    public String getSupportedAction() {
        return "edit_task";
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
            // Update fields
            if (json.has("newTitle")) task.setTitle(json.getString("newTitle"));
            if (json.has("description")) task.setDescription(json.getString("description"));
            if (json.has("status")) task.setStatus(json.getString("status"));
            if (json.has("deadline")) {
                try {
                    String deadlineStr = json.getString("deadline");
                    Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(deadlineStr);
                    task.setDeadline(deadline);
                } catch (Exception e) { /* ignore parse errors */ }
            }
            if (json.has("assignTo")) {
                String email = json.getString("assignTo");
                User user = userService.findByEmail(email).orElse(null);
                task.setAssignTo(user);
            }
            if (json.has("column")) {
                String columnName = json.getString("column");
                List<TaskColumn> columns = taskColumnService.getAllTaskColumns();
                TaskColumn column = columns.stream().filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(columnName)).findFirst().orElse(null);
                task.setTaskColumn(column);
            }
            taskService.updateTask(task.getId(), task);
            return "Task updated";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 