package com.example.wildlife.controller;

import com.example.wildlife.model.Detection;
import com.example.wildlife.service.DetectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(DetectionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DetectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DetectionService detectionService;

    @MockBean
    private software.amazon.awssdk.services.s3.S3Client s3Client;

    @Test
    public void testGetAll() throws Exception {
        List<Detection> detections = Arrays.asList(new Detection("Tiger", "DANGEROUS", 0.95), new Detection("Rabbit", "SAFE", 0.80));
        when(detectionService.getAll()).thenReturn(detections);

        mockMvc.perform(get("/api/detect/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetDangerous() throws Exception {
        List<Detection> dangerous = Arrays.asList(new Detection("Lion", "DANGEROUS", 0.90));
        when(detectionService.getDangerous()).thenReturn(dangerous);

        mockMvc.perform(get("/api/detect/dangerous"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("DANGEROUS"));
    }

    @Test
    public void testGetLatestVideo() throws Exception {
        Detection detection = new Detection("Elephant", "DANGEROUS", 0.85);
        detection.setVideoUrl("video.mp4");
        when(detectionService.getAll()).thenReturn(Arrays.asList(detection));

        mockMvc.perform(get("/api/detect/latest-video"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.animalName").value("Elephant"));
    }

    @Test
    public void testGetLatestVideo_NoVideo() throws Exception {
        Detection detection = new Detection("Cat", "SAFE", 0.70);
        when(detectionService.getAll()).thenReturn(Arrays.asList(detection));

        mockMvc.perform(get("/api/detect/latest-video"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}