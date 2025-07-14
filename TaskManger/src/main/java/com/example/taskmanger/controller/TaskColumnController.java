package com.example.taskmanger.controller;

import com.example.taskmanger.model.Board;
import com.example.taskmanger.model.TaskColumn;
import com.example.taskmanger.repository.BoardRepository;
import com.example.taskmanger.service.TaskColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/task-columns")
public class TaskColumnController {
    @Autowired
    private TaskColumnService taskColumnService;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/all")
    public List<TaskColumn> getAllTaskColumns() {
        return taskColumnService.getAllTaskColumns();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TaskColumn> getTaskColumnById(@PathVariable int id) {
        return taskColumnService.getTaskColumnById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-board/{boardId}")
    public List<TaskColumn> getColumnsByBoard(@PathVariable int boardId) {
        return taskColumnService.getColumnsByBoard(boardId);
    }

    @PostMapping("/add")
    public TaskColumn createTaskColumn(@RequestBody TaskColumn taskColumn) {
        if (taskColumn.getBoard() != null && taskColumn.getBoard().getId() != 0) {
            Board board = boardRepository.findById(taskColumn.getBoard().getId()).orElse(null);
            taskColumn.setBoard(board);
        } else {
            taskColumn.setBoard(null);
        }
        return taskColumnService.createTaskColumn(taskColumn);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskColumn> updateTaskColumn(@PathVariable int id, @RequestBody TaskColumn taskColumn) {
        if (!taskColumnService.getTaskColumnById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(taskColumnService.updateTaskColumn(id, taskColumn));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTaskColumn(@PathVariable int id) {
        if (!taskColumnService.getTaskColumnById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        taskColumnService.deleteTaskColumn(id);
        return ResponseEntity.noContent().build();
    }
} 