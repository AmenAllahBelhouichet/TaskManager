package com.example.taskmanger.controller;

import com.example.taskmanger.model.Project;
import com.example.taskmanger.model.User;
import com.example.taskmanger.service.ProjectService;
import com.example.taskmanger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable int id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{ownerId}")
    public List<Project> getProjectsByOwnerId(@PathVariable int ownerId) {
        System.out.println("Fetching projects for ownerId: " + ownerId);
        List<Project> projects = projectService.getProjectsByOwnerId(ownerId);
        System.out.println("Found projects: " + projects);
        return projects;
    }

    @PostMapping("/add")
    public Project createProject(@RequestBody Project project) {
        // If owner is present and has an id, fetch the user and set as owner
        if (project.getOwner() != null && project.getOwner().getId() != 0) {
            Optional<User> ownerOpt = userService.getUserById(project.getOwner().getId());
            if (ownerOpt.isPresent()) {
                project.setOwner(ownerOpt.get());
                System.out.println("Owner set to: " + ownerOpt.get());
            } else {
                System.out.println("User with id " + project.getOwner().getId() + " not found");
                project.setOwner(null);
            }
        } else {
            System.out.println("No owner id provided");
            project.setOwner(null); // or handle as needed
        }
        return projectService.createProject(project);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable int id, @RequestBody Project project) {
        if (!projectService.getProjectById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable int id) {
        if (!projectService.getProjectById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
} 