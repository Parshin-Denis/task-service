package com.example.TaskService.dto;

import com.example.TaskService.model.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class UpsertUserRequest {

    private String name;

    private String email;

    private String password;

    private Set<RoleType> roles;
}
