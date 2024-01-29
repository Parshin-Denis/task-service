package com.example.TaskService.controller;

import com.example.TaskService.AbstractTest;
import com.example.TaskService.dto.UpsertUserRequest;
import com.example.TaskService.dto.UserResponse;
import com.example.TaskService.model.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserControllerTest extends AbstractTest {

    @Test
    @WithMockUser
    public void whenGetAllUsers_thenReturnUsersList() {

        List<UserResponse> expectedResponse = List.of(
                new UserResponse(MANAGER_ID, "Manager", "manager@email.com",
                        Collections.singleton(RoleType.ROLE_MANAGER)),
                new UserResponse(USER1_ID, "User1", "user1@email.com",
                        Collections.singleton(RoleType.ROLE_USER)),
                new UserResponse(USER2_ID, "User2", "user2@email.com",
                        Collections.singleton(RoleType.ROLE_USER))
        );

        webTestClient.get().uri("/api/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(3)
                .contains(expectedResponse.toArray(UserResponse[]::new));
    }

    @Test
    @WithMockUser
    public void whenGetUserById_thenReturnUser() {
        UserResponse expectedResponse = new UserResponse(USER1_ID, "User1", "user1@email.com",
                Collections.singleton(RoleType.ROLE_USER));
        webTestClient.get().uri("/api/user/{id}", USER1_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    public void whenCreateUser_thenReturnNewUser() {
        StepVerifier.create(userRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();
        UpsertUserRequest upsertUserRequest = new UpsertUserRequest();
        upsertUserRequest.setName("newUser");
        upsertUserRequest.setEmail("newUser@email.com");
        upsertUserRequest.setPassword("12345");
        upsertUserRequest.setRoles(Collections.singleton(RoleType.ROLE_USER));

        webTestClient.post().uri("/api/user")
                .body(Mono.just(upsertUserRequest), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .value(responseModel -> {
                            assertEquals("newUser", responseModel.getName());
                            assertEquals("newUser@email.com", responseModel.getEmail());
                            assertNotNull(responseModel.getId());
                            assertEquals(Collections.singleton(RoleType.ROLE_USER), responseModel.getRoles());
                        }
                );

        StepVerifier.create(userRepository.count())
                .expectNext(4L)
                .expectComplete()
                .verify();
    }

    @Test
    @WithMockUser
    public void whenUpdateUser_thenReturnUpdatedUser() {
        UpsertUserRequest upsertUserRequest = new UpsertUserRequest();
        upsertUserRequest.setName("updatedUser");
        upsertUserRequest.setEmail("updatedUser@email.com");
        upsertUserRequest.setPassword("12345");
        upsertUserRequest.setRoles(Collections.singleton(RoleType.ROLE_MANAGER));

        UserResponse expectedResponse = new UserResponse(USER1_ID, "updatedUser",
                "updatedUser@email.com", Collections.singleton(RoleType.ROLE_MANAGER));

        webTestClient.put().uri("/api/user/{id}", USER1_ID)
                .body(Mono.just(upsertUserRequest), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    @WithMockUser
    public void whenDeleteUserById_thenReturnNoContent() {
        StepVerifier.create(userRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();

        webTestClient.delete().uri("/api/user/{id}", MANAGER_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
    }
}
