package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.TaskColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMovementAgentHandler implements AIAgentHandler {
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskColumnService taskColumnService;

    @Override
    public String getSupportedAction() {
        return "move_task";
    }

    @Override
    public String handle(String targetEntity) {
        try {
            // Try to parse by ID first: move_task:taskId:columnId
            String[] parts = targetEntity.split(":");
            if (parts.length == 3) {
                try {
                    int taskId = Integer.parseInt(parts[1]);
                    int columnId = Integer.parseInt(parts[2]);
                    TaskColumn column = taskColumnService.getTaskColumnById(columnId).orElseThrow();
                    taskService.moveTaskToColumn(taskId, column);
                    return "Task moved by ID";
                } catch (NumberFormatException e) {
                    // Fallback to name-based
                }
            }
            // Name-based: move_task:taskTitle:columnName
            if (parts.length == 3) {
                String taskTitle = parts[1];
                String columnName = parts[2];
                // Find task by title (case-insensitive, first match)
                List<Task> tasks = taskService.getAllTasks();
                Task task = tasks.stream()
                        .filter(t -> t.getTitle() != null && t.getTitle().equalsIgnoreCase(taskTitle))
                        .findFirst().orElse(null);
                if (task == null) return "Task with title '" + taskTitle + "' not found";
                // Find column by name (case-insensitive, first match)
                List<TaskColumn> columns = taskColumnService.getAllTaskColumns();
                TaskColumn column = columns.stream()
                        .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(columnName))
                        .findFirst().orElse(null);
                if (column == null) return "Column with name '" + columnName + "' not found";
                taskService.moveTaskToColumn(task.getId(), column);
                return "Task moved by name";
            }
            return "Invalid move_task format. Use move_task:taskId:columnId or move_task:taskTitle:columnName";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
} 