package com.example.wildlife.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DetectionTest {

    @Test
    public void testNoArgsConstructor() {
        Detection detection = new Detection();

        assertThat(detection).isNotNull();
        assertThat(detection.getId()).isNull();
        assertThat(detection.getAnimalName()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime time = LocalDateTime.now();
        Detection detection = new Detection();
        detection.setId(1L);
        detection.setAnimalName("Tiger");
        detection.setCategory("DANGEROUS");
        detection.setConfidence(0.95);
        detection.setLocation("Jungle");
        detection.setLatitude(10.0);
        detection.setLongitude(20.0);
        detection.setTime(time);

        assertThat(detection.getId()).isEqualTo(1L);
        assertThat(detection.getAnimalName()).isEqualTo("Tiger");
        assertThat(detection.getCategory()).isEqualTo("DANGEROUS");
        assertThat(detection.getConfidence()).isEqualTo(0.95);
        assertThat(detection.getLocation()).isEqualTo("Jungle");
        assertThat(detection.getLatitude()).isEqualTo(10.0);
        assertThat(detection.getLongitude()).isEqualTo(20.0);
        assertThat(detection.getTime()).isEqualTo(time);
    }

    @Test
    public void testSettersAndGetters() {
        Detection detection = new Detection();

        detection.setAnimalName("Lion");
        detection.setAlertSent(true);
        detection.setAlertStatus("SENT");

        assertThat(detection.getAnimalName()).isEqualTo("Lion");
        assertThat(detection.isAlertSent()).isTrue();
        assertThat(detection.getAlertStatus()).isEqualTo("SENT");
    }

    @Test
    public void testThreeArgsConstructor() {
        Detection detection = new Detection("Elephant", "SAFE", 0.80);

        assertThat(detection.getAnimalName()).isEqualTo("Elephant");
        assertThat(detection.getCategory()).isEqualTo("SAFE");
        assertThat(detection.getConfidence()).isEqualTo(0.80);
    }
}
