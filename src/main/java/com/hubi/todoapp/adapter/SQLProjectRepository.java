package com.hubi.todoapp.adapter;

import com.hubi.todoapp.model.Project;
import com.hubi.todoapp.model.TaskGroup;
import com.hubi.todoapp.model.repository.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SQLProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

    @Override
    boolean existsById(@Param("id") Integer id);
}
