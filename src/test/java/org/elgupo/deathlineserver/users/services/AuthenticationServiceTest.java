package org.elgupo.deathlineserver.users.services;

import org.elgupo.deathlineserver.users.AuthException;
import org.elgupo.deathlineserver.users.models.AuthRequest;
import org.elgupo.deathlineserver.users.models.AuthResponse;
import org.elgupo.deathlineserver.users.repositories.UserEntity;
import org.elgupo.deathlineserver.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Tests")
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    private AuthRequest validAuthRequest;
    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        validAuthRequest = new AuthRequest();
        validAuthRequest.email = "test@example.com";
        validAuthRequest.password = "password123";

        mockUser = new UserEntity("test@example.com", "password123");
        mockUser.setId(1L);
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void shouldRegisterNewUser() {
        // Given
        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        // When
        AuthResponse response = authenticationService.register(validAuthRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id);
        assertEquals("OK", response.message);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void shouldLoginWithValidCredentials() throws AuthException {
        // Given
        when(userRepository.findByEmail(validAuthRequest.email)).thenReturn(mockUser);

        // When
        AuthResponse response = authenticationService.login(validAuthRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id);
        assertEquals("OK", response.message);
        verify(userRepository, times(1)).findByEmail(validAuthRequest.email);
    }

    @Test
    @DisplayName("Should throw AuthException when user not found during login")
    void shouldThrowAuthExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByEmail(validAuthRequest.email)).thenReturn(null);

        // When & Then
        assertThrows(AuthException.class, () -> authenticationService.login(validAuthRequest));
        verify(userRepository, times(1)).findByEmail(validAuthRequest.email);
    }

    @Test
    @DisplayName("Should throw AuthException when password is incorrect")
    void shouldThrowAuthExceptionWhenPasswordIncorrect() {
        // Given
        UserEntity userWithDifferentPassword = new UserEntity("test@example.com", "wrongpassword");
        userWithDifferentPassword.setId(1L);
        when(userRepository.findByEmail(validAuthRequest.email)).thenReturn(userWithDifferentPassword);

        // When & Then
        assertThrows(AuthException.class, () -> authenticationService.login(validAuthRequest));
        verify(userRepository, times(1)).findByEmail(validAuthRequest.email);
    }

    @Test
    @DisplayName("Should handle null email in login request")
    void shouldHandleNullEmailInLogin() {
        // Given
        AuthRequest requestWithNullEmail = new AuthRequest();
        requestWithNullEmail.email = null;
        requestWithNullEmail.password = "password123";

        when(userRepository.findByEmail(null)).thenReturn(null);

        // When & Then
        assertThrows(AuthException.class, () -> authenticationService.login(requestWithNullEmail));
    }

    @Test
    @DisplayName("Should handle null password in login request")
    void shouldHandleNullPasswordInLogin() {
        // Given
        AuthRequest requestWithNullPassword = new AuthRequest();
        requestWithNullPassword.email = "test@example.com";
        requestWithNullPassword.password = null;

        when(userRepository.findByEmail(validAuthRequest.email)).thenReturn(mockUser);

        // When & Then
        assertThrows(AuthException.class, () -> authenticationService.login(requestWithNullPassword));
    }

    @Test
    @DisplayName("Should handle empty email in registration")
    void shouldHandleEmptyEmailInRegistration() {
        // Given
        AuthRequest requestWithEmptyEmail = new AuthRequest();
        requestWithEmptyEmail.email = "";
        requestWithEmptyEmail.password = "password123";

        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        // When
        AuthResponse response = authenticationService.register(requestWithEmptyEmail);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should handle empty password in registration")
    void shouldHandleEmptyPasswordInRegistration() {
        // Given
        AuthRequest requestWithEmptyPassword = new AuthRequest();
        requestWithEmptyPassword.email = "test@example.com";
        requestWithEmptyPassword.password = "";

        when(userRepository.save(any(UserEntity.class))).thenReturn(mockUser);

        // When
        AuthResponse response = authenticationService.register(requestWithEmptyPassword);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}
