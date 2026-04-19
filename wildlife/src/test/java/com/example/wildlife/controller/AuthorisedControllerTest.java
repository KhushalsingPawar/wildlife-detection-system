package com.example.wildlife.controller;

import com.example.wildlife.model.AuthorisedAnimal;
import com.example.wildlife.repository.AuthorisedAnimalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AuthorisedController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorisedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorisedAnimalRepository repo;

    @Test
    public void testAddAuthorised() throws Exception {
        when(repo.existsByName(anyString())).thenReturn(false);
        when(repo.save(any(AuthorisedAnimal.class))).thenReturn(new AuthorisedAnimal(1L, "elephant"));

        mockMvc.perform(post("/api/authorised/add")
                .param("animal", "elephant"))
                .andExpect(status().isOk())
                .andExpect(content().string("elephant added to authorised list"));
    }

    @Test
    public void testAddAuthorised_AlreadyExists() throws Exception {
        when(repo.existsByName(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/authorised/add")
                .param("animal", "elephant"))
                .andExpect(status().isOk())
                .andExpect(content().string("elephant added to authorised list"));
    }

    @Test
    public void testRemoveAuthorised() throws Exception {
        mockMvc.perform(post("/api/authorised/remove")
                .param("animal", "elephant"))
                .andExpect(status().isOk())
                .andExpect(content().string("elephant removed from authorised list"));
    }

    @Test
    public void testGetAll() throws Exception {
        when(repo.findAll()).thenReturn(Arrays.asList(
                new AuthorisedAnimal(1L, "cat"),
                new AuthorisedAnimal(2L, "dog")
        ));

        mockMvc.perform(get("/api/authorised/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}