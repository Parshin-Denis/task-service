package com.example.TaskService.dto;

import com.example.TaskService.model.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {

    private String id;

    private String name;

    private String email;

    private Set<RoleType> roles;
}
