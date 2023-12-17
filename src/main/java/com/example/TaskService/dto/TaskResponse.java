package com.example.TaskService.dto;

import com.example.TaskService.model.TaskStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class TaskResponse {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus taskStatus;

    private UserResponse author;

    private UserResponse assignee;

    private Set<UserResponse> observers;

}
