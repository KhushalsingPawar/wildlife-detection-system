package com.example.wildlife.controller;

import com.example.wildlife.config.JwtUtil;
import com.example.wildlife.model.User;
import com.example.wildlife.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String buildAuthJson(String name, String email, String password) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "name", name,
                "email", email,
                "password", password
        ));
    }

    @Test
    public void testSignup_Success() throws Exception {
        User user = new User("Jane", "jane@example.com", "123");

        when(authService.signup(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildAuthJson("Jane", "jane@example.com", "123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    public void testSignup_UserExists() throws Exception {
        User user = new User("John", "john@example.com", "password");

        when(authService.signup(any(User.class)))
                .thenThrow(new RuntimeException("User already exists"));

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildAuthJson("John", "john@example.com", "password")))
                .andExpect(status().isBadRequest()); // better practice
    }

    @Test
    public void testLogin_Success() throws Exception {
        User user = new User("John", "john@example.com", "password");
        user.setId(1L);

        when(authService.login(anyString(), anyString())).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), any(Long.class))).thenReturn("mock-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildAuthJson("John", "john@example.com", "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.token").value("mock-token"));
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        User user = new User("John", "john@example.com", "wrong");

        when(authService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(buildAuthJson("John", "john@example.com", "wrong")))
                .andExpect(status().isBadRequest()); // better than 500
    }
}