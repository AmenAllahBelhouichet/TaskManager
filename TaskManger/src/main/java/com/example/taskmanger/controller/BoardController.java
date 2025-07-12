package com.example.taskmanger.controller;

import com.example.taskmanger.model.Board;
import com.example.taskmanger.model.Project;
import com.example.taskmanger.repository.ProjectRepository;
import com.example.taskmanger.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/all")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable int id) {
        return boardService.getBoardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-project/{projectId}")
    public List<Board> getBoardsByProjectId(@PathVariable int projectId) {
        return boardService.getBoardsByProjectId(projectId);
    }

    @PostMapping("/add")
    public Board createBoard(@RequestBody Board board) {
        System.out.println("Received board for creation: " + board);
        if (board.getProject() != null && board.getProject().getId() != 0) {
            Project project = projectRepository.findById(board.getProject().getId()).orElse(null);
            System.out.println("Fetched project: " + project);
            if (project != null) {
                board.setProject(project);
            } else {
                System.out.println("Project with id " + board.getProject().getId() + " not found. Setting project to null.");
                board.setProject(null);
            }
        } else {
            System.out.println("No project id provided. Setting project to null.");
            board.setProject(null);
        }
        Board saved = boardService.createBoard(board);
        System.out.println("Saved board: " + saved);
        return saved;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable int id, @RequestBody Board board) {
        if (!boardService.getBoardById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boardService.updateBoard(id, board));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable int id) {
        if (!boardService.getBoardById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
} 