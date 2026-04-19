package com.example.wildlife.service;

import com.example.wildlife.model.Detection;
import com.example.wildlife.repository.DetectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetectionServiceTest {

    @Mock
    private DetectionRepository repo;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private DetectionService detectionService;

    @Test
    public void testSaveDetection_DangerousAnimal() {
        Detection detection = new Detection("Tiger", "DANGEROUS", 0.95);
        detection.setLocation("Jungle");
        when(repo.save(any(Detection.class))).thenReturn(detection);
        when(smsService.sendSms(anyString())).thenReturn(true);

        Detection result = detectionService.saveDetection(detection);

        assertThat(result.getCategory()).isEqualTo("DANGEROUS");
        assertThat(result.isAlertSent()).isTrue();
        assertThat(result.getAlertStatus()).isEqualTo("SENT");
        verify(messagingTemplate).convertAndSend(eq("/topic/alerts"), any(Detection.class));
    }

    @Test
    public void testSaveDetection_SafeAnimal() {
        Detection detection = new Detection("Rabbit", "SAFE", 0.80);
        when(repo.save(any(Detection.class))).thenReturn(detection);

        Detection result = detectionService.saveDetection(detection);

        assertThat(result.getCategory()).isEqualTo("SAFE");
        assertThat(result.getAlertStatus()).isEqualTo("NO_ALERT");
        verify(smsService, never()).sendSms(anyString());
    }

    @Test
    public void testSaveDetection_SmsFailure() {
        Detection detection = new Detection("Lion", "DANGEROUS", 0.90);
        when(repo.save(any(Detection.class))).thenReturn(detection);
        when(smsService.sendSms(anyString())).thenReturn(false);

        Detection result = detectionService.saveDetection(detection);

        assertThat(result.isAlertSent()).isFalse();
        assertThat(result.getAlertStatus()).isEqualTo("FAILED");
    }

    @Test
    public void testGetAll() {
        List<Detection> detections = Arrays.asList(new Detection(), new Detection());
        when(repo.findAll()).thenReturn(detections);

        List<Detection> result = detectionService.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    public void testGetDangerous() {
        List<Detection> dangerous = Arrays.asList(new Detection("Tiger", "DANGEROUS", 0.95));
        when(repo.findByCategory("DANGEROUS")).thenReturn(dangerous);

        List<Detection> result = detectionService.getDangerous();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("DANGEROUS");
    }
}