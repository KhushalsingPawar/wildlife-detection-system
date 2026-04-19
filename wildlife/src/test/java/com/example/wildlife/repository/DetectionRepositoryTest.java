package com.example.wildlife.repository;

import com.example.wildlife.model.Detection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DetectionRepositoryTest {

    @Autowired
    private DetectionRepository detectionRepository;

    @Test
    public void testSaveDetection() {
        Detection detection = new Detection();
        detection.setAnimalName("Tiger");
        detection.setCategory("DANGEROUS");
        detection.setConfidence(0.95);
        detection.setLocation("Jungle");
        detection.setLatitude(10.0);
        detection.setLongitude(20.0);
        detection.setTime(LocalDateTime.now());

        Detection saved = detectionRepository.save(detection);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAnimalName()).isEqualTo("Tiger");
    }

    @Test
    public void testFindAll() {
        List<Detection> detections = detectionRepository.findAll();
        assertThat(detections).isNotNull();
    }

    @Test
    public void testFindByCategory() {
        List<Detection> dangerous = detectionRepository.findByCategory("DANGEROUS");
        assertThat(dangerous).isNotNull();
    }

    @Test
    public void testFindByCategory_NoResults() {
        List<Detection> none = detectionRepository.findByCategory("NONEXISTENT");
        assertThat(none).isEmpty();
    }
}
