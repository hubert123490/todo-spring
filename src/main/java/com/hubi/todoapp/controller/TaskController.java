package com.hubi.todoapp.controller;

import com.hubi.todoapp.logic.TaskService;
import com.hubi.todoapp.model.Task;
import com.hubi.todoapp.model.repository.TaskRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;
    private final TaskService service;

    TaskController(final TaskRepository repository, TaskService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping( params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Task>>> readAllTasks() {
        logger.warn("Exposing all the tasks!!!");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @ResponseBody
    @RequestMapping("/greetings")
    public String getString()
    {
        return JSONObject.quote("Hello World");
    }


    @GetMapping("/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    ResponseEntity<Task> updateTask(@PathVariable("id") int taskId, @RequestBody @Valid Task toUpdate) {
        if (!repository.existsById(taskId)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(taskId).ifPresent(task -> {
            task.updateFrom(toUpdate);
            repository.save(task);
        });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<Task> toggleTask(@PathVariable("id") int taskId) {
        if (!repository.existsById(taskId)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(taskId).ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }


    // ##################################################################################
    /* @GetMapping("/tasks/{id}")
    ResponseEntity<Optional<Task>> findTaskById(@PathVariable("id") int taskId){
        if(repository.existsById(taskId)){
            return ResponseEntity.ok(repository.findById(taskId));
        }else
        {
            return ResponseEntity.notFound().build();
        }
    }*/

   /* @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        if(repository.existsById(toCreate.getId())){
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(toCreate.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(toCreate);
    }*/
}
