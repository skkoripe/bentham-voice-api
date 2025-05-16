package com.bentham.voiceagent.controller;

import com.bentham.voiceagent.model.TranscriptionResult;
import com.bentham.voiceagent.service.TranscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Controller for handling WebSocket transcription requests.
 * This controller provides endpoints for starting, stopping, and streaming audio for transcription.
 */
@Controller
public class TranscriptionWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptionWebSocketController.class);
    
    private final TranscriptionService transcriptionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, String> sessionMap = new ConcurrentHashMap<>();
    
    @Autowired
    public TranscriptionWebSocketController(TranscriptionService transcriptionService, 
                                           SimpMessagingTemplate messagingTemplate) {
        this.transcriptionService = transcriptionService;
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Starts a new transcription session.
     *
     * @param headerAccessor the message headers
     * @return a map containing the session ID
     */
    @MessageMapping("/transcription/start")
    @SendTo("/topic/session")
    public Map<String, String> startTranscription(SimpMessageHeaderAccessor headerAccessor) {
        String webSocketSessionId = headerAccessor.getSessionId();
        logger.info("Starting transcription session for WebSocket session: {}", webSocketSessionId);
        
        // Start a new streaming transcription session
        String transcriptionSessionId = transcriptionService.startStreamingTranscription();
        
        // Store the mapping between WebSocket session ID and transcription session ID
        sessionMap.put(webSocketSessionId, transcriptionSessionId);
        
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", transcriptionSessionId);
        return response;
    }
    
    /**
     * Processes audio data for transcription.
     *
     * @param audioData the audio data bytes
     * @param headerAccessor the message headers
     */
    @MessageMapping("/transcription/audio")
    public void processAudio(@Payload byte[] audioData, SimpMessageHeaderAccessor headerAccessor) {
        String webSocketSessionId = headerAccessor.getSessionId();
        String transcriptionSessionId = sessionMap.get(webSocketSessionId);
        
        if (transcriptionSessionId == null) {
            logger.warn("No transcription session found for WebSocket session: {}", webSocketSessionId);
            return;
        }
        
        logger.debug("Received audio data for session {}: {} bytes", transcriptionSessionId, audioData.length);
        
        // Send the audio data to the transcription service
        transcriptionService.sendAudioChunk(transcriptionSessionId, audioData);
        
        // Get the latest transcription and send it back to the client
        String transcription = transcriptionService.getLatestTranscription(transcriptionSessionId);
        if (transcription != null && !transcription.isEmpty()) {
            // Send to general topic instead of user-specific destination
            TranscriptionResult result = TranscriptionResult.partial(
                transcriptionSessionId, 
                transcription
            );
            messagingTemplate.convertAndSend(
                    "/topic/transcription",
                    result
            );
        }
    }
    
    /**
     * Stops a transcription session.
     *
     * @param headerAccessor the message headers
     * @return the final transcription result
     */
    @MessageMapping("/transcription/stop")
    @SendTo("/topic/transcription")
    public TranscriptionResult stopTranscription(SimpMessageHeaderAccessor headerAccessor) {
        String webSocketSessionId = headerAccessor.getSessionId();
        String transcriptionSessionId = sessionMap.get(webSocketSessionId);
        
        if (transcriptionSessionId == null) {
            logger.warn("No transcription session found for WebSocket session: {}", webSocketSessionId);
            return TranscriptionResult.final_(null, "No active transcription session", 0.0);
        }
        
        logger.info("Stopping transcription session: {}", transcriptionSessionId);
        
        // Stop the streaming session and get the final transcription
        String finalTranscription = transcriptionService.stopStreamingTranscription(transcriptionSessionId);
        
        // Remove the session mapping
        sessionMap.remove(webSocketSessionId);
        
        // Return a TranscriptionResult instead of a String
        return TranscriptionResult.final_(
            transcriptionSessionId,
            finalTranscription,
            0.95 // Example confidence score
        );
    }
}
