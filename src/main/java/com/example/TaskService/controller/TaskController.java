package com.example.TaskService.controller;

import com.example.TaskService.dto.TaskResponse;
import com.example.TaskService.dto.UpsertTaskRequest;
import com.example.TaskService.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Get all tasks")
    @GetMapping
    public Flux<TaskResponse> findAll(){
        return taskService.findAll();
    }

    @Operation(summary = "Get task by ID")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id){
        return taskService.findById(id);
    }

    @Operation(summary = "Create task")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> create(@RequestBody UpsertTaskRequest upsertTaskRequest){
        return taskService.save(upsertTaskRequest);
    }

    @Operation(summary = "Modify task")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id, @RequestBody UpsertTaskRequest upsertTaskRequest){
        return taskService.update(id, upsertTaskRequest);
    }

    @Operation(summary = "Add observer to task")
    @PutMapping("/add-observer/{id}")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable String id, @RequestParam String observerId){
        return taskService.addObserver(id, observerId);
    }

    @Operation(summary = "Delete task by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id){
        return taskService.delete(id);
    }
}
