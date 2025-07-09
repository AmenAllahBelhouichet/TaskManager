package com.example.taskmanger.service;

import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.repository.TaskColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskColumnService {
    @Autowired
    private TaskColumnRepository taskColumnRepository;

    public List<TaskColumn> getAllTaskColumns() {
        return taskColumnRepository.findAll();
    }

    public Optional<TaskColumn> getTaskColumnById(int id) {
        return taskColumnRepository.findById(id);
    }

    public TaskColumn createTaskColumn(TaskColumn taskColumn) {
        return taskColumnRepository.save(taskColumn);
    }

    public TaskColumn updateTaskColumn(int id, TaskColumn taskColumn) {
        taskColumn.setId(id);
        return taskColumnRepository.save(taskColumn);
    }

    public void deleteTaskColumn(int id) {
        taskColumnRepository.deleteById(id);
    }
} 