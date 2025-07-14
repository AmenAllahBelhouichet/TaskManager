package com.example.taskmanger.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"projects", "assignedTasks", "comments", "password", "role", "name"})
    private User author;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"assignTo", "comments", "taskColumn", "status", "description", "deadline", "title"})
    private Task task;

    private Date createdDate;


}
