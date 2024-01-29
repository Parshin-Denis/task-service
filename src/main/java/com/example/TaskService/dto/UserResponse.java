package com.example.TaskService.dto;

import com.example.TaskService.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;

    private String name;

    private String email;

    private Set<RoleType> roles;
}
