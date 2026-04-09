package com.example.wildlife.dto;

public class DetectionDTO {

    private String animalName;
    private String category;
    private double confidence;
    private String location;
    private String cameraId;
    private String detectedBy;

    // Default constructor
    public DetectionDTO() {}

    // Parameterized constructor (optional)
    public DetectionDTO(String animalName, String category, double confidence,
                        String location, String cameraId, String detectedBy) {
        this.animalName = animalName;
        this.category = category;
        this.confidence = confidence;
        this.location = location;
        this.cameraId = cameraId;
        this.detectedBy = detectedBy;
    }

    // Getters and Setters
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
}