package com.example.taskmanger.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"assignTo", "taskColumn", "comments"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"comments"})
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String title,description,status;
    private Date deadline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assign_to_id")
    private User assignTo;

    @ManyToOne
    @JoinColumn(name = "task_column_id")
    private TaskColumn taskColumn;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comments> comments;


}
