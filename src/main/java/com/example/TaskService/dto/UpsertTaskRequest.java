package com.example.TaskService.dto;

import com.example.TaskService.model.TaskStatus;
import lombok.Data;

@Data
public class UpsertTaskRequest {

    private String name;

    private String description;

    private TaskStatus taskStatus;

    private String authorId;

    private String assigneeId;

}
