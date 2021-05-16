package com.hubi.todoapp.controller;

import com.hubi.todoapp.logic.TaskGroupService;
import com.hubi.todoapp.model.Task;
import com.hubi.todoapp.model.projection.*;
import com.hubi.todoapp.model.repository.TaskGroupRepository;
import com.hubi.todoapp.model.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class TaskGroupController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupRepository repository;
    private final TaskGroupService service;
    private final TaskRepository taskRepository;

    public TaskGroupController(TaskGroupRepository repository, TaskGroupService service, TaskRepository taskRepository) {
        this.repository = repository;
        this.service = service;
        this.taskRepository = taskRepository;
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        GroupReadModel result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        return ResponseEntity.ok(service.readAll());
    }

    @ResponseBody
    @Transactional
    @PatchMapping(value = "/{id}")
    public ResponseEntity<GroupReadModel> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    
    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    ////////////////////////////////// TEMPLATES //////////////////////
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showAllGroups(Model model){
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(@ModelAttribute("group") @Valid GroupWriteModel current,
                      BindingResult bindingResult,
                      Model model) {
        if (bindingResult.hasErrors()) {
            return "groups";
        }
        service.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Dodano grupe!");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new GroupTaskWriteModel());
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return service.readAll();
    }
}
