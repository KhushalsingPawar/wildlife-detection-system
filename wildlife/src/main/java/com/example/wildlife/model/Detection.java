package com.example.wildlife.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Detection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String animalName;
    private String category;
    private double confidence;

    private String location;
    private double latitude;
    private double longitude;

    private String imageUrl;
    private String videoUrl;

    // Device info
    private String cameraId;
    private String detectedBy;

    // Time
    private LocalDateTime time;

    // Alert tracking
    private boolean alertSent;
    private String alertType;
    private String alertStatus;

    // Status
    private String status;

    // ===== GETTERS AND SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getDetectedBy() {
        return detectedBy;
    }

    public void setDetectedBy(String detectedBy) {
        this.detectedBy = detectedBy;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isAlertSent() {
        return alertSent;
    }

    public void setAlertSent(boolean alertSent) {
        this.alertSent = alertSent;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Constructors
    public Detection() {}

    public Detection(String animalName, String category, double confidence) {
        this.animalName = animalName;
        this.category = category;
        this.confidence = confidence;
    }
}