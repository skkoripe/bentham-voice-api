package com.bentham.voiceagent.controller;

import com.bentham.voiceagent.model.TranscriptionResult;
import com.bentham.voiceagent.service.TranscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for the TranscriptionWebSocketController class.
 */
@ExtendWith(MockitoExtension.class)
class TranscriptionWebSocketControllerTest {

    @Mock
    private TranscriptionService transcriptionService;
    
    @Mock
    private SimpMessagingTemplate messagingTemplate;
    
    @Mock
    private SimpMessageHeaderAccessor headerAccessor;
    
    private TranscriptionWebSocketController controller;
    
    @BeforeEach
    void setUp() {
        controller = new TranscriptionWebSocketController(transcriptionService, messagingTemplate);
        when(headerAccessor.getSessionId()).thenReturn("test-websocket-session");
    }
    
    @Test
    void startTranscriptionShouldReturnSessionId() {
        // Arrange
        when(transcriptionService.startStreamingTranscription()).thenReturn("test-transcription-session");
        
        // Act
        Map<String, String> response = controller.startTranscription(headerAccessor);
        
        // Assert
        assertEquals("test-transcription-session", response.get("sessionId"));
    }
    
    @Test
    void processAudioShouldSendTranscriptionResult() {
        // Arrange
        byte[] audioData = new byte[1024];
        
        // Setup session mapping
        Map<String, String> sessionMap = new ConcurrentHashMap<>();
        sessionMap.put("test-websocket-session", "test-transcription-session");
        ReflectionTestUtils.setField(controller, "sessionMap", sessionMap);
        
        when(transcriptionService.getLatestTranscription("test-transcription-session"))
            .thenReturn("Test transcription");
        
        // Act
        controller.processAudio(audioData, headerAccessor);
        
        // Assert
        verify(transcriptionService).sendAudioChunk("test-transcription-session", audioData);
        
        ArgumentCaptor<TranscriptionResult> resultCaptor = ArgumentCaptor.forClass(TranscriptionResult.class);
        verify(messagingTemplate).convertAndSend(eq("/topic/transcription"), resultCaptor.capture());
        
        TranscriptionResult capturedResult = resultCaptor.getValue();
        assertEquals("test-transcription-session", capturedResult.getSessionId());
        assertEquals("Test transcription", capturedResult.getTranscript());
        assertTrue(capturedResult.isPartial());
    }
    
    @Test
    void processAudioShouldNotSendEmptyTranscription() {
        // Arrange
        byte[] audioData = new byte[1024];
        
        // Setup session mapping
        Map<String, String> sessionMap = new ConcurrentHashMap<>();
        sessionMap.put("test-websocket-session", "test-transcription-session");
        ReflectionTestUtils.setField(controller, "sessionMap", sessionMap);
        
        when(transcriptionService.getLatestTranscription("test-transcription-session"))
            .thenReturn("");
        
        // Act
        controller.processAudio(audioData, headerAccessor);
        
        // Assert
        verify(transcriptionService).sendAudioChunk("test-transcription-session", audioData);
        verify(messagingTemplate, never()).convertAndSend(eq("/topic/transcription"), any(TranscriptionResult.class));
    }
    
    @Test
    void processAudioShouldHandleMissingSession() {
        // Arrange
        byte[] audioData = new byte[1024];
        
        // Act
        controller.processAudio(audioData, headerAccessor);
        
        // Assert
        verify(transcriptionService, never()).sendAudioChunk(any(), any());
        verify(messagingTemplate, never()).convertAndSend(eq("/topic/transcription"), any(TranscriptionResult.class));
    }
    
    @Test
    void stopTranscriptionShouldReturnFinalResult() {
        // Arrange
        Map<String, String> sessionMap = new ConcurrentHashMap<>();
        sessionMap.put("test-websocket-session", "test-transcription-session");
        ReflectionTestUtils.setField(controller, "sessionMap", sessionMap);
        
        when(transcriptionService.stopStreamingTranscription("test-transcription-session"))
            .thenReturn("Final transcription");
        
        // Act
        TranscriptionResult result = controller.stopTranscription(headerAccessor);
        
        // Assert
        assertEquals("test-transcription-session", result.getSessionId());
        assertEquals("Final transcription", result.getTranscript());
        assertFalse(result.isPartial());
        assertEquals(0.95, result.getConfidence(), 0.01);
        assertTrue(sessionMap.isEmpty());
    }
    
    @Test
    void stopTranscriptionShouldHandleMissingSession() {
        // Act
        TranscriptionResult result = controller.stopTranscription(headerAccessor);
        
        // Assert
        assertNull(result.getSessionId());
        assertEquals("No active transcription session", result.getTranscript());
        assertFalse(result.isPartial());
        assertEquals(0.0, result.getConfidence());
    }
}
