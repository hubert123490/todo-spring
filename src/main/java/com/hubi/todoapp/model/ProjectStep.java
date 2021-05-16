package com.hubi.todoapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Data
@Table(name = "project_steps")
public class ProjectStep {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int id;
    @NotBlank(message = "project step's description cannot be null!")
    private String description;
    private long DaysToDeadline;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
