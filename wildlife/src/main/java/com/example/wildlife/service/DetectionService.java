package com.example.wildlife.service;

import com.example.wildlife.model.Detection;
import com.example.wildlife.repository.DetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DetectionService {

    @Autowired
    private DetectionRepository repo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TwilioSmsService smsService;

    private final List<String> dangerousDefault = Arrays.asList("tiger", "lion", "leopard", "bear", "elephant");
    private final Map<String, Long> lastDetected = new HashMap<>();
    private final long COOLDOWN = 30000; // 30 seconds

    public Detection saveDetection(Detection d) {
        String animal = (d.getAnimalName() != null) ? d.getAnimalName().toLowerCase() : "";
        long currentTime = System.currentTimeMillis();

        if (dangerousDefault.contains(animal)) {
            d.setCategory("DANGEROUS");
            
            // Basic Rate Limiting for alerts
            Long lastTime = lastDetected.getOrDefault(animal, 0L);
            if (currentTime - lastTime > COOLDOWN) {
                // Send SMS alert
                String smsMessage = String.format("🚨 ALERT: Dangerous animal detected - %s at %s (Confidence: %.2f)",
                        d.getAnimalName(), d.getLocation(), d.getConfidence());
                try {
                    smsService.sendSms(smsMessage);
                    d.setAlertSent(true);
                    d.setAlertStatus("SENT");
                } catch (Exception e) {
                    d.setAlertSent(false);
                    d.setAlertStatus("FAILED: " + e.getMessage());
                }

                // Send WebSocket alert to all connected clients
                messagingTemplate.convertAndSend("/topic/alerts", d);

                lastDetected.put(animal, currentTime);
            } else {
                d.setAlertStatus("COOLDOWN_SKIPPED");
            }
        } else {
            d.setCategory("SAFE");
            d.setAlertStatus("NO_ALERT");
        }

        d.setStatus("ACTIVE");
        return repo.save(d);
    }

    public List<Detection> getAll() {
        return repo.findAll();
    }

    public List<Detection> getDangerous() {
        return repo.findByCategory("DANGEROUS");
    }
}