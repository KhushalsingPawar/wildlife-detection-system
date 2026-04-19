package com.example.wildlife.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(TacticalController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TacticalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testActivateLights() throws Exception {
        mockMvc.perform(post("/api/tactical/lights"))
                .andExpect(status().isOk())
                .andExpect(content().string("Lights activated"));
    }

    @Test
    public void testActivateAlarm() throws Exception {
        mockMvc.perform(post("/api/tactical/alarm"))
                .andExpect(status().isOk())
                .andExpect(content().string("Alarm activated"));
    }
}