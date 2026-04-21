package com.example.wildlife.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // frontend subscribe here
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // MUST MATCH VITE PROXY
                .setAllowedOriginPatterns(
                    "http://localhost:*",      // Allow all localhost ports
                    "http://127.0.0.1:*",
                    "https://wildlife-detection-system-eight.vercel.app"    
                )
                .withSockJS(); // important
    }
}