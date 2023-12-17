package com.example.TaskService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus taskStatus;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

    @ReadOnlyProperty
    private User author;

    @ReadOnlyProperty
    private User assignee;

    @ReadOnlyProperty
    private Set<User> observers;

    public Task setUsers(Object[] users){
        author = (User)users[0];
        assignee = (User)users[1];
        observers = new HashSet<>();
        for (int i = 2; i < users.length; i++){
            observers.add((User)users[i]);
        }
        return this;
    }

}
