package com.example.TaskService.controller;

import com.example.TaskService.AbstractTest;
import com.example.TaskService.dto.TaskResponse;
import com.example.TaskService.dto.UpsertTaskRequest;
import com.example.TaskService.dto.UserResponse;
import com.example.TaskService.model.RoleType;
import com.example.TaskService.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TaskControllerTest extends AbstractTest {
    @Test
    @WithMockUser
    public void whenGetTaskById_thenReturnTask() {
        TaskResponse expectedResponse = new TaskResponse();
        expectedResponse.setId(TASK_ID);
        expectedResponse.setName("Task");
        expectedResponse.setDescription("Description");
        expectedResponse.setTaskStatus(TaskStatus.TODO);
        expectedResponse.setCreatedAt(TASK_CREATED);
        expectedResponse.setUpdatedAt(TASK_UPDATED);
        expectedResponse.setAuthor(new UserResponse(MANAGER_ID, "Manager", "manager@email.com",
                Collections.singleton(RoleType.ROLE_MANAGER)));
        expectedResponse.setAssignee(new UserResponse(USER1_ID, "User1", "user1@email.com",
                Collections.singleton(RoleType.ROLE_USER)));
        expectedResponse.setObservers(Collections.singleton(new UserResponse(USER2_ID, "User2", "user2@email.com",
                Collections.singleton(RoleType.ROLE_USER))));
        webTestClient.get().uri("/api/task/{id}", TASK_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "reactiveUserDetailsServiceImpl", value = "Manager",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenCreateTask_thenReturnNewTask() {
        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();

        UpsertTaskRequest upsertTaskRequest = new UpsertTaskRequest();
        upsertTaskRequest.setName("newTask");
        upsertTaskRequest.setDescription("newDescription");
        upsertTaskRequest.setTaskStatus(TaskStatus.TODO);
        upsertTaskRequest.setAssigneeId(USER2_ID);

        webTestClient.post().uri("/api/task")
                .body(Mono.just(upsertTaskRequest), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponse.class)
                .value(responseModel -> {
                            assertNotNull(responseModel.getId());
                            assertTrue(responseModel.getCreatedAt().isBefore(Instant.now()));
                            assertEquals("newTask", responseModel.getName());
                            assertEquals("newDescription", responseModel.getDescription());
                            assertEquals(MANAGER_ID, responseModel.getAuthor().getId());
                            assertEquals(USER2_ID, responseModel.getAssignee().getId());
                            assertEquals(TaskStatus.TODO, responseModel.getTaskStatus());
                        }
                );

        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
    }

    @Test
    @WithMockUser
    public void whenAddObserver_thenReturnUpdatedTask() {
        Set<UserResponse> expectedObservers= Set.of(
                new UserResponse(USER2_ID, "User2", "user2@email.com",
                        Collections.singleton(RoleType.ROLE_USER)),
                new UserResponse(USER1_ID, "User1", "user1@email.com",
                        Collections.singleton(RoleType.ROLE_USER))
                );
        webTestClient.put().uri("/api/task/add-observer/{id}?observerId={observerId}", TASK_ID, USER1_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(responseModel -> {
                    assertNotNull(responseModel.getId());
                    assertTrue(responseModel.getUpdatedAt().isBefore(Instant.now()));
                    assertEquals("Task", responseModel.getName());
                    assertEquals("Description", responseModel.getDescription());
                    assertEquals(MANAGER_ID, responseModel.getAuthor().getId());
                    assertEquals(USER1_ID, responseModel.getAssignee().getId());
                    assertEquals(TaskStatus.TODO, responseModel.getTaskStatus());
                    assertEquals(TASK_CREATED, responseModel.getCreatedAt());
                    assertTrue(responseModel.getObservers().containsAll(expectedObservers));
                });
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void whenDeleteById_thenReturnNoContent() {
        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();

        webTestClient.delete().uri("/api/task/{id}", TASK_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(taskRepository.count())
                .expectNext(0L)
                .expectComplete()
                .verify();
    }
}
