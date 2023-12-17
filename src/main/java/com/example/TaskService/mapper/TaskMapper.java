package com.example.TaskService.mapper;

import com.example.TaskService.dto.TaskResponse;
import com.example.TaskService.dto.UpsertTaskRequest;
import com.example.TaskService.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface TaskMapper {

    Task requestToTask(UpsertTaskRequest upsertTaskRequest);

    TaskResponse taskToResponse(Task task);
}
