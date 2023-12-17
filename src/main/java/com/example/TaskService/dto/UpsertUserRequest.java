package com.example.TaskService.dto;

import lombok.Data;

@Data
public class UpsertUserRequest {

    private String name;

    private String email;
}
