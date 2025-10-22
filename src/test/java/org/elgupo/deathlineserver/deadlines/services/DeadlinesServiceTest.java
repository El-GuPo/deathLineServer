package org.elgupo.deathlineserver.deadlines.services;

import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.elgupo.deathlineserver.deadlines.repository.DeadlineEntity;
import org.elgupo.deathlineserver.deadlines.repository.DeadlinesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeadlinesService Tests")
class DeadlinesServiceTest {

    @Mock
    private DeadlinesRepository deadlinesRepository;

    @InjectMocks
    private DeadlinesService deadlinesService;

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
            new DeadlineDto(2L, "Deadline 2", "Description 2", Instant.now().plus(2, ChronoUnit.DAYS)),
            new DeadlineDto(3L, "Deadline 3", "Description 3", Instant.now().plus(3, ChronoUnit.DAYS))
        );
    }

    @Test
    @DisplayName("Should get all deadlines for user when no date filters provided")
    void shouldGetAllDeadlinesForUserWithoutFilters() {
        // Given
        when(deadlinesRepository.findByUserId(userId)).thenReturn(testDeadlines);

        // When
        List<DeadlineDto> result = deadlinesService.getDeadlinesForUser(userId, null, null);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(testDeadlines, result);
        verify(deadlinesRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should filter deadlines by 'from' date")
    void shouldFilterDeadlinesByFromDate() {
        // Given
        Instant fromDate = Instant.now().plus(1, ChronoUnit.HOURS);
        when(deadlinesRepository.findByUserId(userId)).thenReturn(testDeadlines);

        // When
        List<DeadlineDto> result = deadlinesService.getDeadlinesForUser(userId, fromDate, null);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size()); // All deadlines are after fromDate
        verify(deadlinesRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should filter deadlines by 'to' date")
    void shouldFilterDeadlinesByToDate() {
        // Given
        Instant toDate = Instant.now().plus(1, ChronoUnit.HOURS);
        when(deadlinesRepository.findByUserId(userId)).thenReturn(testDeadlines);

        // When
        List<DeadlineDto> result = deadlinesService.getDeadlinesForUser(userId, null, toDate);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size()); // No deadlines are before toDate
        verify(deadlinesRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should filter deadlines by both 'from' and 'to' dates")
    void shouldFilterDeadlinesByBothDates() {
        // Given
        Instant fromDate = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant toDate = Instant.now().plus(2, ChronoUnit.DAYS);
        when(deadlinesRepository.findByUserId(userId)).thenReturn(testDeadlines);

        // When
        List<DeadlineDto> result = deadlinesService.getDeadlinesForUser(userId, fromDate, toDate);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size()); // Only deadlines 1 and 2 are in range
        verify(deadlinesRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should return empty list when user has no deadlines")
    void shouldReturnEmptyListWhenNoDeadlines() {
        // Given
        when(deadlinesRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<DeadlineDto> result = deadlinesService.getDeadlinesForUser(userId, null, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(deadlinesRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should successfully create deadline for user")
    void shouldCreateDeadlineForUser() {
        // Given
        when(deadlinesRepository.save(any(DeadlineEntity.class))).thenReturn(testDeadlineEntity);

        // When
        DeadlineEntity result = deadlinesService.createDeadlineForUser(userId, testDeadlineDto);

        // Then
        assertNotNull(result);
        assertEquals(testDeadlineEntity.getDeadlineId(), result.getDeadlineId());
        assertEquals(userId, result.getUserId());
        assertEquals(testDeadlineDto.getName(), result.getName());
        assertEquals(testDeadlineDto.getDescription(), result.getDescription());
        assertEquals(testDeadlineDto.getDeadline(), result.getDeadline());
        verify(deadlinesRepository, times(1)).save(any(DeadlineEntity.class));
    }

    @Test
    @DisplayName("Should successfully delete deadline for user")
    void shouldDeleteDeadlineForUser() {
        // Given
        Long deadlineId = 1L;
        doNothing().when(deadlinesRepository).deleteByDeadlineId(deadlineId);

        // When
        deadlinesService.deleteDeadlineForUser(deadlineId);

        // Then
        verify(deadlinesRepository, times(1)).deleteByDeadlineId(deadlineId);
    }

    @Test
    @DisplayName("Should handle null deadline name in creation")
    void shouldHandleNullDeadlineNameInCreation() {
        // Given
        DeadlineDto deadlineWithNullName = new DeadlineDto(
            1L,
            null,
            "Test Description",
            Instant.now().plus(1, ChronoUnit.DAYS)
        );
        when(deadlinesRepository.save(any(DeadlineEntity.class))).thenReturn(testDeadlineEntity);

        // When
        DeadlineEntity result = deadlinesService.createDeadlineForUser(userId, deadlineWithNullName);

        // Then
        assertNotNull(result);
        verify(deadlinesRepository, times(1)).save(any(DeadlineEntity.class));
    }

    @Test
    @DisplayName("Should handle null deadline description in creation")
    void shouldHandleNullDeadlineDescriptionInCreation() {
        // Given
        DeadlineDto deadlineWithNullDescription = new DeadlineDto(
            1L,
            "Test Name",
            null,
            Instant.now().plus(1, ChronoUnit.DAYS)
        );
        when(deadlinesRepository.save(any(DeadlineEntity.class))).thenReturn(testDeadlineEntity);

        // When
        DeadlineEntity result = deadlinesService.createDeadlineForUser(userId, deadlineWithNullDescription);

        // Then
        assertNotNull(result);
        verify(deadlinesRepository, times(1)).save(any(DeadlineEntity.class));
    }

    @Test
    @DisplayName("Should handle past deadline date in creation")
    void shouldHandlePastDeadlineDateInCreation() {
        // Given
        DeadlineDto deadlineWithPastDate = new DeadlineDto(
            1L,
            "Test Name",
            "Test Description",
            Instant.now().minus(1, ChronoUnit.DAYS)
        );
        when(deadlinesRepository.save(any(DeadlineEntity.class))).thenReturn(testDeadlineEntity);

        // When
        DeadlineEntity result = deadlinesService.createDeadlineForUser(userId, deadlineWithPastDate);

        // Then
        assertNotNull(result);
        verify(deadlinesRepository, times(1)).save(any(DeadlineEntity.class));
    }

    @Test
    @DisplayName("Should handle edge case with exact date boundaries")
    void shouldHandleExactDateBoundaries() {
        // Given
        Instant exactTime = Instant.now().plus(1, ChronoUnit.DAYS);
        List<DeadlineDto> deadlinesWithExactTime = Arrays.asList(
            new DeadlineDto(1L, "Deadline 1", "Description 1", exactTime),
            new DeadlineDto(2L, "Deadline 2", "Description 2", exactTime.plus(1, ChronoUnit.SECONDS))
        );
        when(deadlinesRepository.findByUserId(userId)).thenReturn(deadlinesWithExactTime);

        // When
        List<DeadlineDto> result = deadlinesService.getDeadlinesForUser(userId, exactTime, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size()); // Only deadline 2 is after exactTime
        verify(deadlinesRepository, times(1)).findByUserId(userId);
    }
}
