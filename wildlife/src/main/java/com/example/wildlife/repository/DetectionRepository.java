package com.example.wildlife.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wildlife.model.Detection;

import java.util.List;

public interface DetectionRepository extends JpaRepository<Detection, Long> {
    List<Detection> findByCategory(String category);
}