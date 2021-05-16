package com.hubi.todoapp.model.repository;

import com.hubi.todoapp.model.Project;
import com.hubi.todoapp.model.TaskGroup;

import java.util.List;
import java.util.Optional;


public interface ProjectRepository {
    List<Project> findAll();

    Optional<Project> findById(Integer id);

    Project save(Project entity);
}
