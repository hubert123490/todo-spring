package com.hubi.todoapp.logic;

import com.hubi.todoapp.TaskConfigurationProperties;
import com.hubi.todoapp.model.Project;
import com.hubi.todoapp.model.projection.GroupReadModel;
import com.hubi.todoapp.model.projection.GroupTaskWriteModel;
import com.hubi.todoapp.model.projection.GroupWriteModel;
import com.hubi.todoapp.model.projection.ProjectWriteModel;
import com.hubi.todoapp.model.repository.ProjectRepository;
import com.hubi.todoapp.model.repository.TaskGroupRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository groupRepository;
    private TaskConfigurationProperties config;
    private TaskGroupService service;

    ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository,
                   final TaskGroupService service,
                    final TaskConfigurationProperties config) {
        this.repository = repository;
        this.groupRepository = taskGroupRepository;
        this.service = service;
        this.config = config;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {

        return repository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime daysToDeadline) {
        if (!config.getTemplate().isAllowMultipleTasksFromTemplate() && groupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed!");
        }
        GroupReadModel result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(project.getSteps().stream().map(projectStep -> {
                                var task = new GroupTaskWriteModel();
                                task.setDescription(projectStep.getDescription());
                                task.setDeadline(daysToDeadline.plusDays(projectStep.getDaysToDeadline()));
                                return task;
                            }
                            ).collect(Collectors.toList())
                    );
                    return service.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found!"));
        return result;
    }


}
