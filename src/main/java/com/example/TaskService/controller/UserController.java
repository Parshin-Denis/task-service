package com.example.TaskService.controller;

import com.example.TaskService.dto.UserResponse;
import com.example.TaskService.dto.UpsertUserRequest;
import com.example.TaskService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping
    public Flux<UserResponse> findAll(){
        return userService.findAll();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id){
        return userService.findById(id);
    }

    @Operation(summary = "Create user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> create(@RequestBody UpsertUserRequest upsertUserRequest){
        return userService.save(upsertUserRequest);
    }

    @Operation(summary = "Modify user")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String  id, @RequestBody UpsertUserRequest upsertUserRequest){
        return userService.update(id, upsertUserRequest);
    }

    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id){
        return userService.delete(id);
    }
}
