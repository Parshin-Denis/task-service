package com.example.TaskService.service;

import com.example.TaskService.dto.TaskResponse;
import com.example.TaskService.dto.UpsertTaskRequest;
import com.example.TaskService.mapper.TaskMapper;
import com.example.TaskService.model.Task;
import com.example.TaskService.model.User;
import com.example.TaskService.repository.TaskRepository;
import com.example.TaskService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final TaskMapper taskMapper;

    public Flux<TaskResponse> findAll() {
        return taskRepository.findAll()
                .flatMap(this::getFullTask)
                .map(taskMapper::taskToResponse);
    }

    public Mono<ResponseEntity<TaskResponse>> findById(String id) {
        return taskRepository.findById(id)
                .flatMap(this::getFullTask)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<TaskResponse> save(UpsertTaskRequest upsertTaskRequest) {
        Task task = taskMapper.requestToTask(upsertTaskRequest);
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setObserverIds(new HashSet<>());
        return taskRepository.save(task)
                .flatMap(this::getFullTask)
                .map(taskMapper::taskToResponse);
    }

    public Mono<ResponseEntity<TaskResponse>> update(String id, UpsertTaskRequest upsertTaskRequest) {
        return taskRepository.findById(id)
                .flatMap(task -> {
                    Task newTask = taskMapper.requestToTask(upsertTaskRequest);
                    newTask.setId(id);
                    newTask.setUpdatedAt(Instant.now());
                    newTask.setCreatedAt(task.getCreatedAt());
                    newTask.setObserverIds(task.getObserverIds());
                    return taskRepository.save(newTask);
                })
                .flatMap(this::getFullTask)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<TaskResponse>> addObserver(String taskId, String observerId){
        return taskRepository.findById(taskId)
                .flatMap(task -> {
                    task.setUpdatedAt(Instant.now());
                    if (task.getObserverIds() == null){
                        task.setObserverIds(new HashSet<>());
                    }
                    task.getObserverIds().add(observerId);
                    return taskRepository.save(task);
                })
                .flatMap(this::getFullTask)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<Void> delete(String id){
        return taskRepository.deleteById(id);
    }

    public Mono<Task> getFullTask(Task task) {
        List<Mono<User>> users = new ArrayList<>();

        users.add(task.getAuthorId() == null ? Mono.just(new User()) : userRepository.findById(task.getAuthorId())
                .defaultIfEmpty(new User(task.getAuthorId(), null, null)));

        users.add(task.getAssigneeId() == null ? Mono.just(new User()) : userRepository.findById(task.getAssigneeId())
                .defaultIfEmpty(new User(task.getAssigneeId(), null, null)));

        if (task.getObserverIds() != null) {
            task.getObserverIds().forEach(observerId -> users.add(userRepository.findById(observerId)
                    .defaultIfEmpty(new User(observerId, null, null))));
        }
        return Mono.zip(users, task::setUsers);
    }

}
