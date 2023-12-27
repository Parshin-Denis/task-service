package com.example.TaskService.service;

import com.example.TaskService.dto.UpsertUserRequest;
import com.example.TaskService.dto.UserResponse;
import com.example.TaskService.mapper.UserMapper;
import com.example.TaskService.model.User;
import com.example.TaskService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public Flux<UserResponse> findAll() {
        return userRepository.findAll().map(userMapper::userToResponse);
    }

    public Mono<ResponseEntity<UserResponse>> findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Mono<UserResponse> save(UpsertUserRequest upsertUserRequest) {
        User user = userMapper.requestToUser(upsertUserRequest);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).map(userMapper::userToResponse);
    }

    public Mono<ResponseEntity<UserResponse>> update(String id, UpsertUserRequest upsertUserRequest) {
        return userRepository.findById(id)
                .flatMap(user -> {
                    User newUser = userMapper.requestToUser(upsertUserRequest);
                    newUser.setId(id);
                    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    return userRepository.save(newUser);
                })
                .map(userMapper::userToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    public Mono<ResponseEntity<Void>> delete(String id) {
        return userRepository.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
