package com.fiap.workshop.management.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.workshop.management.application.dto.auth.AuthRequest;
import com.fiap.workshop.management.infrastructure.persistence.repository.UserJpaRepository;
import com.fiap.workshop.management.infrastructure.security.UserJpaEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Auth Integration")
class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setUpUser() {
        userJpaRepository.save(new UserJpaEntity(
                UUID.randomUUID().toString(),
                "auth-admin@test.com",
                passwordEncoder.encode("password123"),
                "ROLE_ADMIN",
                true));
    }

    @Test
    @DisplayName("should return JWT token when credentials are valid")
    void shouldReturnJwtTokenWhenCredentialsAreValid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AuthRequest("auth-admin@test.com", "password123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyOrNullString())));
    }

    @Test
    @DisplayName("should return 401 when password is wrong")
    void shouldReturn401WhenPasswordIsWrong() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AuthRequest("auth-admin@test.com", "wrongpass"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return 401 when user does not exist")
    void shouldReturn401WhenUserDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AuthRequest("nobody@test.com", "password123"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return 401 when accessing protected endpoint without token")
    void shouldReturn401WhenAccessingProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/service-orders"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return 200 when accessing protected endpoint with valid token")
    void shouldReturn200WhenAccessingProtectedEndpointWithValidToken() throws Exception {
        String token = loginAndGetToken("auth-admin@test.com", "password123");

        mockMvc.perform(get("/api/v1/service-orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthRequest(email, password))))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }
}
