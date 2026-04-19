package com.example.wildlife.service;

import com.example.wildlife.model.Detection;
import com.example.wildlife.repository.DetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
public class DetectionService {

    private static final Logger logger = LoggerFactory.getLogger(DetectionService.class);

    @Autowired
    private DetectionRepository repo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SmsService smsService;

    private final List<String> dangerousDefault = Arrays.asList("tiger", "lion", "leopard", "bear", "elephant");
    private final Map<String, Long> lastDetected = new HashMap<>();
    private final long COOLDOWN = 30000; // 30 seconds

    public Detection saveDetection(Detection d) {
        String animal = (d.getAnimalName() != null) ? d.getAnimalName().toLowerCase() : "";
        long currentTime = System.currentTimeMillis();

        logger.info("Processing detection for animal: {}, location: {}", d.getAnimalName(), d.getLocation());

        if (dangerousDefault.contains(animal)) {
            d.setCategory("DANGEROUS");
            logger.warn("DANGEROUS animal detected: {} at {}", d.getAnimalName(), d.getLocation());
            
            // Basic Rate Limiting for alerts
            Long lastTime = lastDetected.getOrDefault(animal, 0L);
            if (currentTime - lastTime > COOLDOWN) {
                // Send SMS alert
                String smsMessage = String.format("🚨 ALERT: Dangerous animal detected - %s at %s (Confidence: %.2f)",
                        d.getAnimalName(), d.getLocation(), d.getConfidence());
                logger.info("Attempting to send SMS alert for {}", d.getAnimalName());
                boolean smsSent = smsService.sendSms(smsMessage);
                if (smsSent) {
                    d.setAlertSent(true);
                    d.setAlertStatus("SENT");
                    logger.info("Alert SMS sent successfully for {}", d.getAnimalName());
                } else {
                    d.setAlertSent(false);
                    d.setAlertStatus("FAILED");
                    logger.error("Failed to send SMS alert for {}", d.getAnimalName());
                }

                // Send WebSocket alert to all connected clients
                try {
                    messagingTemplate.convertAndSend("/topic/alerts", d);
                    logger.info("WebSocket alert sent for {}", d.getAnimalName());
                } catch (Exception e) {
                    logger.error("Failed to send WebSocket alert: {}", e.getMessage(), e);
                }

                lastDetected.put(animal, currentTime);
            } else {
                d.setAlertStatus("COOLDOWN_SKIPPED");
                logger.debug("Alert skipped for {} due to cooldown", animal);
            }
        } else {
            d.setCategory("SAFE");
            d.setAlertStatus("NO_ALERT");
            logger.debug("Safe animal detected: {}", d.getAnimalName());
        }

        d.setStatus("ACTIVE");
        Detection saved = repo.save(d);
        logger.info("Detection saved to database with ID: {}", saved.getId());
        return saved;
    }

    public List<Detection> getAll() {
        return repo.findAll();
    }

    public List<Detection> getDangerous() {
        return repo.findByCategory("DANGEROUS");
    }
}