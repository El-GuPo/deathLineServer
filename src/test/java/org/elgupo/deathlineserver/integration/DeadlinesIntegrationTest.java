package org.elgupo.deathlineserver.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elgupo.deathlineserver.TestDataFactory;
import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.elgupo.deathlineserver.deadlines.repository.DeadlinesRepository;
import org.elgupo.deathlineserver.deadlines.repository.DeadlineEntity;
import org.elgupo.deathlineserver.users.models.AuthRequest;
import org.elgupo.deathlineserver.users.models.AuthResponse;
import org.elgupo.deathlineserver.users.repositories.UserRepository;
import org.elgupo.deathlineserver.users.repositories.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Deadlines Integration Tests")
class DeadlinesIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeadlinesRepository deadlinesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private UserEntity testUser;
    private Long userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test user
        testUser = TestDataFactory.createValidUserEntity();
        testUser = userRepository.save(testUser);
        userId = testUser.getId();
    }

    @Test
    @DisplayName("Should create and retrieve deadlines for user")
    void shouldCreateAndRetrieveDeadlinesForUser() throws Exception {
        // Given
        DeadlineDto deadlineDto = TestDataFactory.createValidDeadlineDto();

        // When - Create deadline
        mockMvc.perform(post("/deadlines/create_deadline_for_user")
                .param("userId", userId.toString())
                .param("deadline", objectMapper.writeValueAsString(deadlineDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deadline_name").value(deadlineDto.getName()))
                .andExpect(jsonPath("$.deadline_description").value(deadlineDto.getDescription()));

        // When - Retrieve deadlines
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].deadline_name").value(deadlineDto.getName()));

        // Verify in database
        List<DeadlineEntity> savedDeadlines = deadlinesRepository.findByUserId(userId);
        assertEquals(1, savedDeadlines.size());
        assertEquals(deadlineDto.getName(), savedDeadlines.get(0).getName());
    }

    @Test
    @DisplayName("Should filter deadlines by date range")
    void shouldFilterDeadlinesByDateRange() throws Exception {
        // Given - Create multiple deadlines with different dates
        DeadlineDto pastDeadline = TestDataFactory.createPastDeadline();
        DeadlineDto futureDeadline = TestDataFactory.createFutureDeadline();
        DeadlineDto currentDeadline = TestDataFactory.createValidDeadlineDto();

        // Create deadlines
        deadlinesRepository.save(TestDataFactory.createDeadlineEntity(
            userId, pastDeadline.getName(), pastDeadline.getDescription(), 
            pastDeadline.getDeadline(), Instant.now(), 1L));
        
        deadlinesRepository.save(TestDataFactory.createDeadlineEntity(
            userId, futureDeadline.getName(), futureDeadline.getDescription(), 
            futureDeadline.getDeadline(), Instant.now(), 2L));
        
        deadlinesRepository.save(TestDataFactory.createDeadlineEntity(
            userId, currentDeadline.getName(), currentDeadline.getDescription(), 
            currentDeadline.getDeadline(), Instant.now(), 3L));

        // When - Filter by date range (from now to 5 days from now)
        Instant fromDate = Instant.now();
        Instant toDate = Instant.now().plus(5, ChronoUnit.DAYS);

        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString())
                .param("from", fromDate.toString())
                .param("to", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2)); // Only current and future deadlines
    }

    @Test
    @DisplayName("Should delete deadline successfully")
    void shouldDeleteDeadlineSuccessfully() throws Exception {
        // Given - Create a deadline
        DeadlineEntity deadlineEntity = TestDataFactory.createValidDeadlineEntity();
        deadlineEntity.setUserId(userId);
        deadlineEntity = deadlinesRepository.save(deadlineEntity);
        Long deadlineId = deadlineEntity.getDeadlineId();

        // Verify deadline exists
        List<DeadlineEntity> deadlinesBefore = deadlinesRepository.findByUserId(userId);
        assertEquals(1, deadlinesBefore.size());

        // When - Delete deadline
        mockMvc.perform(post("/deadlines/delete_deadline_for_user")
                .param("deadlineId", deadlineId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then - Verify deadline is deleted
        List<DeadlineEntity> deadlinesAfter = deadlinesRepository.findByUserId(userId);
        assertEquals(0, deadlinesAfter.size());
    }

    @Test
    @DisplayName("Should handle user registration and authentication flow")
    void shouldHandleUserRegistrationAndAuthenticationFlow() throws Exception {
        // Given
        AuthRequest authRequest = TestDataFactory.createValidAuthRequest();

        // When - Register user
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.id").exists());

        // When - Login user
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Should handle multiple users with separate deadlines")
    void shouldHandleMultipleUsersWithSeparateDeadlines() throws Exception {
        // Given - Create second user
        UserEntity secondUser = TestDataFactory.createUserEntity("user2@example.com", "password456", null);
        secondUser = userRepository.save(secondUser);
        Long secondUserId = secondUser.getId();

        // Create deadlines for both users
        DeadlineEntity deadline1 = TestDataFactory.createDeadlineEntity(
            userId, "User 1 Deadline", "Description 1", 
            Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), 1L);
        deadlinesRepository.save(deadline1);

        DeadlineEntity deadline2 = TestDataFactory.createDeadlineEntity(
            secondUserId, "User 2 Deadline", "Description 2", 
            Instant.now().plus(1, ChronoUnit.DAYS), Instant.now(), 2L);
        deadlinesRepository.save(deadline2);

        // When - Get deadlines for first user
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].deadline_name").value("User 1 Deadline"));

        // When - Get deadlines for second user
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", secondUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].deadline_name").value("User 2 Deadline"));
    }

    @Test
    @DisplayName("Should handle edge cases with null and empty values")
    void shouldHandleEdgeCasesWithNullAndEmptyValues() throws Exception {
        // Given - Create deadline with null description
        DeadlineDto deadlineWithNullDescription = TestDataFactory.createDeadlineWithNullDescription();

        // When - Create deadline with null description
        mockMvc.perform(post("/deadlines/create_deadline_for_user")
                .param("userId", userId.toString())
                .param("deadline", objectMapper.writeValueAsString(deadlineWithNullDescription))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify deadline was created
        List<DeadlineEntity> deadlines = deadlinesRepository.findByUserId(userId);
        assertEquals(1, deadlines.size());
        assertNull(deadlines.get(0).getDescription());
    }
}
