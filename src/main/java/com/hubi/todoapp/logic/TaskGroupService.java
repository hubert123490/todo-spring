package com.hubi.todoapp.logic;

import com.hubi.todoapp.model.Project;
import com.hubi.todoapp.model.TaskGroup;
import com.hubi.todoapp.model.projection.GroupReadModel;
import com.hubi.todoapp.model.projection.GroupWriteModel;
import com.hubi.todoapp.model.repository.TaskGroupRepository;
import com.hubi.todoapp.model.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source, Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream().map(GroupReadModel::new).collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks! Done all the tasks first.");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Task group with given id not found!"));
        result.setDone(!result.isDone());
        repository.save(result);
    }


}
