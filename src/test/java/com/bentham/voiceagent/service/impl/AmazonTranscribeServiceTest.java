package com.bentham.voiceagent.service.impl;

import com.bentham.voiceagent.config.TranscribeConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.transcribe.TranscribeClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the AmazonTranscribeService class.
 */
@SpringBootTest
class AmazonTranscribeServiceTest {

    private TranscribeClient transcribeClient;
    private TranscribeConfig transcribeConfig;
    private AwsCredentialsProvider credentialsProvider;
    private AmazonTranscribeService transcribeService;
    
    @BeforeEach
    void setUp() {
        // Create mocks
        transcribeClient = mock(TranscribeClient.class);
        
        // Create a test implementation of TranscribeConfig
        transcribeConfig = new TranscribeConfig() {
            @Override
            public String getLanguageCode() {
                return "en-US";
            }
            
            @Override
            public Integer getSampleRate() {
                return 16000;
            }
            
            @Override
            public Boolean getEnablePartialResults() {
                return true;
            }
        };
        
        // Create a test implementation of AwsCredentialsProvider
        credentialsProvider = () -> null;
        
        transcribeService = new AmazonTranscribeService(transcribeClient, transcribeConfig, credentialsProvider);
    }
    
    @Test
    void startStreamingTranscriptionShouldReturnSessionId() {
        // Act
        String sessionId = transcribeService.startStreamingTranscription();
        
        // Assert
        assertNotNull(sessionId, "Session ID should not be null");
        assertFalse(sessionId.isEmpty(), "Session ID should not be empty");
    }
    
    @Test
    void sendAudioChunkShouldNotThrowException() {
        // Arrange
        String sessionId = transcribeService.startStreamingTranscription();
        byte[] audioChunk = new byte[1024];
        
        // Act & Assert
        assertDoesNotThrow(() -> transcribeService.sendAudioChunk(sessionId, audioChunk));
    }
    
    @Test
    void getLatestTranscriptionShouldReturnString() {
        // Arrange
        String sessionId = transcribeService.startStreamingTranscription();
        
        // Act
        String transcription = transcribeService.getLatestTranscription(sessionId);
        
        // Assert
        assertNotNull(transcription);
    }
    
    @Test
    void stopStreamingTranscriptionShouldReturnString() {
        // Arrange
        String sessionId = transcribeService.startStreamingTranscription();
        
        // Act
        String finalTranscription = transcribeService.stopStreamingTranscription(sessionId);
        
        // Assert
        assertNotNull(finalTranscription);
        
        // Verify the session was removed
        Map<String, StreamingTranscriptionHandler> sessions = 
            (Map<String, StreamingTranscriptionHandler>) ReflectionTestUtils.getField(
                transcribeService, "streamingSessions");
        assertFalse(sessions.containsKey(sessionId));
    }
    
    @Test
    void getLatestTranscriptionShouldThrowExceptionForInvalidSession() {
        String invalidSessionId = "invalid-session-id";
        
        assertThrows(IllegalArgumentException.class, 
                () -> transcribeService.getLatestTranscription(invalidSessionId),
                "Getting transcription for invalid session should throw exception");
    }
    
    @Test
    void transcribeAudioShouldReturnCompletableFuture() {
        // Create a test audio stream
        byte[] audioData = new byte[1024];
        InputStream audioStream = new ByteArrayInputStream(audioData);
        
        // Call the method under test
        CompletableFuture<String> future = transcribeService.transcribeAudio(audioStream);
        
        // Verify the result
        assertNotNull(future, "CompletableFuture should not be null");
    }
}
