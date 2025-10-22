package org.elgupo.deathlineserver;

import org.elgupo.deathlineserver.deadlines.controller.dto.DeadlineDto;
import org.elgupo.deathlineserver.deadlines.repository.DeadlineEntity;
import org.elgupo.deathlineserver.users.models.AuthRequest;
import org.elgupo.deathlineserver.users.models.AuthResponse;
import org.elgupo.deathlineserver.users.repositories.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Factory class for creating test data objects.
 * Provides convenient methods to create test entities with realistic data.
 */
public class TestDataFactory {

    // User-related test data
    public static final String TEST_EMAIL = "test@example.com";
    public static final String TEST_PASSWORD = "password123";
    public static final Long TEST_USER_ID = 1L;

    // Deadline-related test data
    public static final String TEST_DEADLINE_NAME = "Test Deadline";
    public static final String TEST_DEADLINE_DESCRIPTION = "Test Description";
    public static final Long TEST_DEADLINE_ID = 1L;

    /**
     * Creates a valid AuthRequest for testing
     */
    public static AuthRequest createValidAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.email = TEST_EMAIL;
        request.password = TEST_PASSWORD;
        return request;
    }

    /**
     * Creates an AuthRequest with custom email and password
     */
    public static AuthRequest createAuthRequest(String email, String password) {
        AuthRequest request = new AuthRequest();
        request.email = email;
        request.password = password;
        return request;
    }

    /**
     * Creates a valid AuthResponse for testing
     */
    public static AuthResponse createValidAuthResponse() {
        return new AuthResponse(TEST_USER_ID, "OK");
    }

    /**
     * Creates an AuthResponse with custom ID and message
     */
    public static AuthResponse createAuthResponse(Long id, String message) {
        return new AuthResponse(id, message);
    }

    /**
     * Creates a valid UserEntity for testing
     */
    public static UserEntity createValidUserEntity() {
        UserEntity user = new UserEntity(TEST_EMAIL, TEST_PASSWORD);
        user.setId(TEST_USER_ID);
        return user;
    }

    /**
     * Creates a UserEntity with custom data
     */
    public static UserEntity createUserEntity(String email, String password, Long id) {
        UserEntity user = new UserEntity(email, password);
        user.setId(id);
        return user;
    }

    /**
     * Creates a valid DeadlineDto for testing
     */
    public static DeadlineDto createValidDeadlineDto() {
        return new DeadlineDto(
            TEST_DEADLINE_ID,
            TEST_DEADLINE_NAME,
            TEST_DEADLINE_DESCRIPTION,
            Instant.now().plus(1, ChronoUnit.DAYS)
        );
    }

    /**
     * Creates a DeadlineDto with custom data
     */
    public static DeadlineDto createDeadlineDto(Long id, String name, String description, Instant deadline) {
        return new DeadlineDto(id, name, description, deadline);
    }

    /**
     * Creates a valid DeadlineEntity for testing
     */
    public static DeadlineEntity createValidDeadlineEntity() {
        DeadlineEntity entity = new DeadlineEntity(
            TEST_USER_ID,
            TEST_DEADLINE_NAME,
            TEST_DEADLINE_DESCRIPTION,
            Instant.now().plus(1, ChronoUnit.DAYS),
            Instant.now()
        );
        entity.setDeadlineId(TEST_DEADLINE_ID);
        return entity;
    }

    /**
     * Creates a DeadlineEntity with custom data
     */
    public static DeadlineEntity createDeadlineEntity(Long userId, String name, String description, 
                                                     Instant deadline, Instant createdAt, Long deadlineId) {
        DeadlineEntity entity = new DeadlineEntity(userId, name, description, deadline, createdAt);
        entity.setDeadlineId(deadlineId);
        return entity;
    }

    /**
     * Creates a list of test deadlines
     */
    public static List<DeadlineDto> createTestDeadlinesList() {
        return Arrays.asList(
            new DeadlineDto(1L, "Deadline 1", "Description 1", Instant.now().plus(1, ChronoUnit.DAYS)),
            new DeadlineDto(2L, "Deadline 2", "Description 2", Instant.now().plus(2, ChronoUnit.DAYS)),
            new DeadlineDto(3L, "Deadline 3", "Description 3", Instant.now().plus(3, ChronoUnit.DAYS))
        );
    }

    /**
     * Creates a deadline with past date
     */
    public static DeadlineDto createPastDeadline() {
        return new DeadlineDto(
            TEST_DEADLINE_ID,
            "Past Deadline",
            "Past Description",
            Instant.now().minus(1, ChronoUnit.DAYS)
        );
    }

    /**
     * Creates a deadline with future date
     */
    public static DeadlineDto createFutureDeadline() {
        return new DeadlineDto(
            TEST_DEADLINE_ID,
            "Future Deadline",
            "Future Description",
            Instant.now().plus(7, ChronoUnit.DAYS)
        );
    }

    /**
     * Creates a deadline with null name
     */
    public static DeadlineDto createDeadlineWithNullName() {
        return new DeadlineDto(
            TEST_DEADLINE_ID,
            null,
            TEST_DEADLINE_DESCRIPTION,
            Instant.now().plus(1, ChronoUnit.DAYS)
        );
    }

    /**
     * Creates a deadline with null description
     */
    public static DeadlineDto createDeadlineWithNullDescription() {
        return new DeadlineDto(
            TEST_DEADLINE_ID,
            TEST_DEADLINE_NAME,
            null,
            Instant.now().plus(1, ChronoUnit.DAYS)
        );
    }

    /**
     * Creates an AuthRequest with null email
     */
    public static AuthRequest createAuthRequestWithNullEmail() {
        AuthRequest request = new AuthRequest();
        request.email = null;
        request.password = TEST_PASSWORD;
        return request;
    }

    /**
     * Creates an AuthRequest with null password
     */
    public static AuthRequest createAuthRequestWithNullPassword() {
        AuthRequest request = new AuthRequest();
        request.email = TEST_EMAIL;
        request.password = null;
        return request;
    }

    /**
     * Creates an AuthRequest with empty email
     */
    public static AuthRequest createAuthRequestWithEmptyEmail() {
        AuthRequest request = new AuthRequest();
        request.email = "";
        request.password = TEST_PASSWORD;
        return request;
    }

    /**
     * Creates an AuthRequest with empty password
     */
    public static AuthRequest createAuthRequestWithEmptyPassword() {
        AuthRequest request = new AuthRequest();
        request.email = TEST_EMAIL;
        request.password = "";
        return request;
    }
}
