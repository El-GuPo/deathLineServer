package org.elgupo.deathlineserver.users.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elgupo.deathlineserver.users.models.AuthRequest;
import org.elgupo.deathlineserver.users.models.AuthResponse;
import org.elgupo.deathlineserver.users.repositories.UserEntity;
import org.elgupo.deathlineserver.users.repositories.UserRepository;
import org.elgupo.deathlineserver.users.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@DisplayName("AuthenticationController Integration Tests")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthRequest validAuthRequest;
    private AuthResponse validAuthResponse;

    @BeforeEach
    void setUp() {
        validAuthRequest = new AuthRequest();
        validAuthRequest.email = "test@example.com";
        validAuthRequest.password = "password123";

        validAuthResponse = new AuthResponse(1L, "OK");
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        when(authenticationService.register(any(AuthRequest.class))).thenReturn(validAuthResponse);

        // When & Then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validAuthRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Given
        when(authenticationService.login(any(AuthRequest.class))).thenReturn(validAuthResponse);

        // When & Then
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validAuthRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("Should handle invalid JSON in registration request")
    void shouldHandleInvalidJsonInRegistration() throws Exception {
        // When & Then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle missing email in registration request")
    void shouldHandleMissingEmailInRegistration() throws Exception {
        // Given
        AuthRequest requestWithoutEmail = new AuthRequest();
        requestWithoutEmail.password = "password123";

        // When & Then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithoutEmail)))
                .andExpect(status().isOk()); // Service handles null email
    }

    @Test
    @DisplayName("Should handle missing password in registration request")
    void shouldHandleMissingPasswordInRegistration() throws Exception {
        // Given
        AuthRequest requestWithoutPassword = new AuthRequest();
        requestWithoutPassword.email = "test@example.com";

        // When & Then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestWithoutPassword)))
                .andExpect(status().isOk()); // Service handles null password
    }

    @Test
    @DisplayName("Should handle empty request body")
    void shouldHandleEmptyRequestBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk()); // Service handles null fields
    }
}
