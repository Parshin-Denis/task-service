package com.example.TaskService.mapper;

import com.example.TaskService.dto.UpsertUserRequest;
import com.example.TaskService.dto.UserResponse;
import com.example.TaskService.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse userToResponse(User user);

    User requestToUser(UpsertUserRequest upsertUserRequest);
}
