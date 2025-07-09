package com.example.taskmanger.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Project> projects;

    @OneToMany(mappedBy = "assignTo")
    @JsonIgnore
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Comments> comments;

    }
