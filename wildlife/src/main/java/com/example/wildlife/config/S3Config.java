package com.example.wildlife.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
                "AKIAUTGY7V7S5NDKVHM6",
                "SKF9/d3dtIlSfb8V44eg8MdlqTbIn7cYxdoE1xw+"
        );
        return S3Client.builder()
                .region(Region.AP_SOUTH_1) // Your bucket region
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }
}