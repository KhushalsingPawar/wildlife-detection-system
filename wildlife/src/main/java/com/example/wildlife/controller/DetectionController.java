package com.example.wildlife.controller;

import com.example.wildlife.model.Detection;
import com.example.wildlife.service.DetectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/detect")
@CrossOrigin(origins = "*")
public class DetectionController {

    private static final Logger logger = LoggerFactory.getLogger(DetectionController.class);

    @Autowired
    private DetectionService service;

    @Autowired
    private S3Client s3Client;


    @Value("${aws.accessKey}") 
    private String accessKey;

    @Value("${aws.secretKey}") 
    private String secretKey;

    @Value("${aws.region}") 
    private String region;
    
    @Value("${aws.bucketName}")
    private String bucketName;    

    @PostMapping("/upload")
    public Detection uploadDetection(
            @RequestParam("animalName") String animalName,
            @RequestParam("category") String category,
            @RequestParam("confidence") double confidence,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "video", required = false) MultipartFile video,
            @RequestParam("location") String location,
            @RequestParam(value = "latitude", required = false) double latitude,
            @RequestParam(value = "longitude", required = false) double longitude,
            @RequestParam("cameraId") String cameraId,
            @RequestParam("detectedBy") String detectedBy
    ) throws IOException {

        Detection detection = new Detection();
        detection.setAnimalName(animalName);
        detection.setCategory(category);
        detection.setConfidence(confidence);
        detection.setLocation(location);
        detection.setLatitude(latitude);
        detection.setLongitude(longitude);
        detection.setCameraId(cameraId);
        detection.setDetectedBy(detectedBy);
        detection.setTime(LocalDateTime.now());

        // Process Image Upload
        if (image != null && !image.isEmpty()) {
            String imageKey = "images/" + System.currentTimeMillis() + "_" + image.getOriginalFilename();
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(imageKey)
                            .acl(software.amazon.awssdk.services.s3.model.ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromBytes(image.getBytes())
            );
            String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, imageKey);
            detection.setImageUrl(imageUrl);
        }

        if (video != null && !video.isEmpty()) {
            String videoKey = "videos/" + System.currentTimeMillis() + "_" + video.getOriginalFilename();
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(videoKey)
                            .acl("public-read")
                            .build(),
                    RequestBody.fromBytes(video.getBytes())
            );
            String videoUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, videoKey);
            detection.setVideoUrl(videoUrl);
        }

        return service.saveDetection(detection);
    }
    

    @GetMapping("/all")
    public List<Detection> all() {
        return service.getAll();
    }

    @GetMapping("/dangerous")
    public List<Detection> dangerous() {
        return service.getDangerous();
    }

    @GetMapping("/latest-video")
    public Detection getLatestVideo() {
    List<Detection> all = service.getAll();
    if (all.isEmpty()) {    
        return null;
    }
    
    // Find the newest detection that actually has a video record
    Detection latest = all.stream()
      .filter(d -> d.getVideoUrl() != null && !d.getVideoUrl().isEmpty())
      .sorted((d1, d2) -> d2.getId().compareTo(d1.getId()))
      .findFirst()
      .orElse(null);

    if (latest != null && !latest.getVideoUrl().startsWith("http")) {
        latest.setVideoUrl("http://localhost:8082/uploads/" + latest.getVideoUrl());
    }

    return latest;
    }
}