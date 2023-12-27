package com.example.TaskService.controller;

import com.example.TaskService.dto.TaskResponse;
import com.example.TaskService.dto.UpsertTaskRequest;
import com.example.TaskService.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Flux<TaskResponse> findAll(){
        return taskService.findAll();
    }

    @Operation(summary = "Get task by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id){
        return taskService.findById(id);
    }

    @Operation(summary = "Create task")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public Mono<TaskResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody UpsertTaskRequest upsertTaskRequest){
        return taskService.save(userDetails.getUsername(), upsertTaskRequest);
    }

    @Operation(summary = "Modify task")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id, @RequestBody UpsertTaskRequest upsertTaskRequest){
        return taskService.update(id, upsertTaskRequest);
    }

    @Operation(summary = "Add observer to task")
    @PutMapping("/add-observer/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@PathVariable String id, @RequestParam String observerId){
        return taskService.addObserver(id, observerId);
    }

    @Operation(summary = "Delete task by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public Mono<Void> delete(@PathVariable String id){
        return taskService.delete(id);
    }
}
