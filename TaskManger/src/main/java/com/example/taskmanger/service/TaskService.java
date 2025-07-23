package com.example.taskmanger.service;

import com.example.taskmanger.model.Task;
import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.repository.TaskRepository;
import com.example.taskmanger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        // Set assignTo to the correct User entity if id is provided
        if (task.getAssignTo() != null && task.getAssignTo().getId() != 0) {
            userRepository.findById(task.getAssignTo().getId()).ifPresent(task::setAssignTo);
        } else {
            task.setAssignTo(null);
        }
        return taskRepository.save(task);
    }

    public Task updateTask(int id, Task task) {
        task.setId(id);
        // Set assignTo to the correct User entity if id is provided
        if (task.getAssignTo() != null && task.getAssignTo().getId() != 0) {
            userRepository.findById(task.getAssignTo().getId()).ifPresent(task::setAssignTo);
        } else {
            task.setAssignTo(null);
        }
        return taskRepository.save(task);
    }

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByColumnId(int columnId) {
        return taskRepository.findByTaskColumn_Id(columnId);
    }

    public Task moveTaskToColumn(int taskId, TaskColumn newColumn) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setTaskColumn(newColumn);
            return taskRepository.save(task);
        }
        throw new IllegalArgumentException("Task not found");
    }
} 