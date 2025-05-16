package com.bentham.voiceagent.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration for WebSocket communication.
 * This class sets up the WebSocket endpoints and message broker for real-time communication.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker for sending messages to clients
        // Messages with destinations starting with /topic will be routed to the broker
        config.enableSimpleBroker("/topic");
        
        // Set the application destination prefix for client-to-server messages
        // Messages with destinations starting with /app will be routed to @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a simple WebSocket endpoint
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Use patterns instead of origins for CORS
                .withSockJS();
    }
}
