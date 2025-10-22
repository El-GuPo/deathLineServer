package org.elgupo.deathlineserver.deadlines.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.elgupo.deathlineserver.deadlines.repository.DeadlineEntity;
import org.elgupo.deathlineserver.deadlines.services.DeadlinesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeadlinesController.class)
@DisplayName("DeadlinesController Integration Tests")
class DeadlinesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeadlinesService deadlinesService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private DeadlineDto testDeadlineDto;
    private DeadlineEntity testDeadlineEntity;
    private List<DeadlineDto> testDeadlines;

    @BeforeEach
    void setUp() {
        userId = 1L;
        
        testDeadlineDto = new DeadlineDto(
            1L,
            "Test Deadline",
            "Test Description",
            Instant.now().plus(1, ChronoUnit.DAYS)
        );

        testDeadlineEntity = new DeadlineEntity(
            userId,
            "Test Deadline",
            "Test Description",
            Instant.now().plus(1, ChronoUnit.DAYS),
            Instant.now()
        );
        testDeadlineEntity.setDeadlineId(1L);

        testDeadlines = Arrays.asList(
            new DeadlineDto(1L, "Deadline 1", "Description 1", Instant.now().plus(1, ChronoUnit.DAYS)),
            new DeadlineDto(2L, "Deadline 2", "Description 2", Instant.now().plus(2, ChronoUnit.DAYS))
        );
    }

    @Test
    @DisplayName("Should get deadlines for user without date filters")
    void shouldGetDeadlinesForUserWithoutFilters() throws Exception {
        // Given
        when(deadlinesService.getDeadlinesForUser(eq(userId), any(), any())).thenReturn(testDeadlines);

        // When & Then
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].deadline_id").value(1L))
                .andExpect(jsonPath("$[0].deadline_name").value("Deadline 1"))
                .andExpect(jsonPath("$[1].deadline_id").value(2L))
                .andExpect(jsonPath("$[1].deadline_name").value("Deadline 2"));
    }

    @Test
    @DisplayName("Should get deadlines for user with date filters")
    void shouldGetDeadlinesForUserWithDateFilters() throws Exception {
        // Given
        Instant fromDate = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant toDate = Instant.now().plus(2, ChronoUnit.DAYS);
        when(deadlinesService.getDeadlinesForUser(eq(userId), eq(fromDate), eq(toDate))).thenReturn(testDeadlines);

        // When & Then
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString())
                .param("from", fromDate.toString())
                .param("to", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should return empty list when user has no deadlines")
    void shouldReturnEmptyListWhenNoDeadlines() throws Exception {
        // Given
        when(deadlinesService.getDeadlinesForUser(eq(userId), any(), any())).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should create deadline for user successfully")
    void shouldCreateDeadlineForUserSuccessfully() throws Exception {
        // Given
        when(deadlinesService.createDeadlineForUser(eq(userId), any(DeadlineDto.class))).thenReturn(testDeadlineEntity);

        // When & Then
        mockMvc.perform(post("/deadlines/create_deadline_for_user")
                .param("userId", userId.toString())
                .param("deadline", objectMapper.writeValueAsString(testDeadlineDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deadline_id").value(1L))
                .andExpect(jsonPath("$.deadline_name").value("Test Deadline"))
                .andExpect(jsonPath("$.deadline_description").value("Test Description"));
    }

    @Test
    @DisplayName("Should delete deadline for user successfully")
    void shouldDeleteDeadlineForUserSuccessfully() throws Exception {
        // Given
        Long deadlineId = 1L;
        // No return value expected for delete operation

        // When & Then
        mockMvc.perform(post("/deadlines/delete_deadline_for_user")
                .param("deadlineId", deadlineId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle missing userId parameter")
    void shouldHandleMissingUserIdParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/deadlines/get_deadlines_for_user"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle invalid userId parameter")
    void shouldHandleInvalidUserIdParameter() throws Exception {
        // When & Then
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle missing deadlineId parameter in delete")
    void shouldHandleMissingDeadlineIdParameter() throws Exception {
        // When & Then
        mockMvc.perform(post("/deadlines/delete_deadline_for_user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle invalid deadlineId parameter in delete")
    void shouldHandleInvalidDeadlineIdParameter() throws Exception {
        // When & Then
        mockMvc.perform(post("/deadlines/delete_deadline_for_user")
                .param("deadlineId", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle invalid date format in filters")
    void shouldHandleInvalidDateFormatInFilters() throws Exception {
        // When & Then
        mockMvc.perform(get("/deadlines/get_deadlines_for_user")
                .param("userId", userId.toString())
                .param("from", "invalid-date")
                .param("to", "invalid-date"))
                .andExpect(status().isBadRequest());
    }
}
