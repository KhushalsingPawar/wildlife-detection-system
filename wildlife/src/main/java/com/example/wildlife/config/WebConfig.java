package com.example.wildlife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This makes your local 'uploads' folder accessible via browser
        // Replace the path below with your actual project uploads folder path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/SpringBoot/wildlife/uploads/"); 
    }
}