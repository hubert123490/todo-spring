package com.hubi.todoapp.adapter;

import com.hubi.todoapp.model.Task;
import com.hubi.todoapp.model.repository.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
interface SQLTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    @Override
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    @Override
    List<Task> findAllByGroup_Id(Integer groupId);
}
