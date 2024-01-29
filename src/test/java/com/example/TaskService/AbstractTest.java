package com.example.TaskService;

import com.example.TaskService.model.RoleType;
import com.example.TaskService.model.Task;
import com.example.TaskService.model.TaskStatus;
import com.example.TaskService.model.User;
import com.example.TaskService.repository.TaskRepository;
import com.example.TaskService.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@AutoConfigureWebTestClient
public class AbstractTest {

    protected static String MANAGER_ID = UUID.randomUUID().toString();
    protected static String USER1_ID = UUID.randomUUID().toString();
    protected static String USER2_ID = UUID.randomUUID().toString();
    protected static String TASK_ID = UUID.randomUUID().toString();
    protected static Instant TASK_CREATED = Instant.parse("2023-12-03T10:15:30.00Z");
    protected static Instant TASK_UPDATED = Instant.parse("2023-01-04T10:15:30.00Z");

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.18")
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected TaskRepository taskRepository;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.saveAll(
                List.of(
                        new User(MANAGER_ID, "Manager", "manager@email.com", "12345",
                                Collections.singleton(RoleType.ROLE_MANAGER)),
                        new User(USER1_ID, "User1", "user1@email.com", "12345",
                                Collections.singleton(RoleType.ROLE_USER)),
                        new User(USER2_ID, "User2", "user2@email.com", "12345",
                                Collections.singleton(RoleType.ROLE_USER))
                )
        ).collectList().block();
        taskRepository.save(
                new Task(TASK_ID, "Task", "Description", TASK_CREATED,
                        TASK_UPDATED, TaskStatus.TODO, MANAGER_ID, USER1_ID, Collections.singleton(USER2_ID),
                        null, null, new HashSet<>())
        ).block();
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll().block();
        taskRepository.deleteAll().block();
    }
}
