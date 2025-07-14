package com.example.taskmanger.controller;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.repository.TaskColumnRepository;
import com.example.taskmanger.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskColumnRepository taskColumnRepository;

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-column/{columnId}")
    public List<Task> getTasksByColumn(@PathVariable int columnId) {
        return taskService.getTasksByColumnId(columnId);
    }

    @PostMapping("/add")
    public Task createTask(@RequestBody Task task) {
        if (task.getTaskColumn() != null && task.getTaskColumn().getId() != 0) {
            var columnOpt = taskColumnRepository.findById(task.getTaskColumn().getId());
            if (columnOpt.isPresent()) {
                task.setTaskColumn(columnOpt.get());
            } else {
                throw new IllegalArgumentException("TaskColumn with id " + task.getTaskColumn().getId() + " not found");
            }
        } else {
            throw new IllegalArgumentException("TaskColumn id must be provided");
        }
        return taskService.createTask(task);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task task) {
        if (!taskService.getTaskById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @PutMapping("/move/{taskId}/to-column/{columnId}")
    public ResponseEntity<Task> moveTaskToColumn(@PathVariable int taskId, @PathVariable int columnId) {
        Optional<TaskColumn> columnOpt = taskColumnRepository.findById(columnId);
        if (columnOpt.isPresent()) {
            try {
                Task updated = taskService.moveTaskToColumn(taskId, columnOpt.get());
                return ResponseEntity.ok(updated);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        if (!taskService.getTaskById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
} 