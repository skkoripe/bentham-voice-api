package com.bentham.voiceagent.service.impl;

import com.bentham.voiceagent.config.TranscribeConfig;
import com.bentham.voiceagent.model.TranscriptionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Handler for streaming transcription.
 * This class manages the processing of audio chunks for streaming transcription.
 * 
 * Note: In a production environment, this would integrate with AWS Transcribe Streaming API.
 * For now, we'll use a simplified implementation for demonstration purposes.
 */
public class StreamingTranscriptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(StreamingTranscriptionHandler.class);
    
    private final String sessionId;
    private final TranscribeConfig config;
    private final Consumer<TranscriptionResult> transcriptionConsumer;
    private final BlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue<>();
    
    private CompletableFuture<Void> streamingFuture;
    private volatile boolean isRunning = false;
    private String latestTranscription = "";
    private int wordCount = 0;
    
    /**
     * Creates a new streaming transcription handler.
     *
     * @param sessionId unique identifier for this streaming session
     * @param config transcribe configuration
     * @param transcriptionConsumer consumer for transcription results
     */
    public StreamingTranscriptionHandler(String sessionId, TranscribeConfig config, Consumer<TranscriptionResult> transcriptionConsumer) {
        this.sessionId = sessionId;
        this.config = config;
        this.transcriptionConsumer = transcriptionConsumer;
    }
    
    /**
     * Starts the streaming transcription.
     *
     * @return CompletableFuture that completes when streaming ends
     */
    public CompletableFuture<Void> startStreaming() {
        if (isRunning) {
            throw new IllegalStateException("Streaming is already in progress for session: " + sessionId);
        }
        
        isRunning = true;
        
        // In a real implementation, this would set up a WebSocket connection to AWS Transcribe
        // For now, we'll simulate the streaming behavior
        streamingFuture = CompletableFuture.runAsync(() -> {
            try {
                logger.info("Started streaming transcription for session: {}", sessionId);
                
                while (isRunning) {
                    // Take audio chunk from queue (blocks if queue is empty)
                    byte[] audioChunk = audioQueue.take();
                    
                    // Process the audio chunk
                    processAudioChunk(audioChunk);
                    
                    // Simulate some processing time
                    Thread.sleep(100);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Streaming interrupted for session: {}", sessionId);
            } catch (Exception e) {
                logger.error("Error in streaming transcription for session: {}", sessionId, e);
                throw new RuntimeException("Streaming transcription failed", e);
            } finally {
                isRunning = false;
                logger.info("Stopped streaming transcription for session: {}", sessionId);
            }
        });
        
        return streamingFuture;
    }
    
    /**
     * Processes an audio chunk and updates the transcription.
     *
     * @param audioChunk the audio data to process
     */
    private void processAudioChunk(byte[] audioChunk) {
        // In a real implementation, this would send the audio chunk to AWS Transcribe
        // and process the response
        
        // For now, we'll simulate receiving a transcription
        if (audioChunk.length > 0) {
            wordCount++;
            
            // Simulate different transcriptions based on word count
            if (wordCount % 3 == 0) {
                // Simulate a partial result
                String partialText = "Processing audio... " + wordCount + " words";
                latestTranscription = partialText;
                
                TranscriptionResult partialResult = TranscriptionResult.partial(sessionId, partialText);
                transcriptionConsumer.accept(partialResult);
                
                // Simulate a final result after a delay
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                String finalText = "This is a simulated transcription with " + wordCount + " words.";
                latestTranscription = finalText;
                
                TranscriptionResult finalResult = TranscriptionResult.final_(sessionId, finalText, 0.95);
                transcriptionConsumer.accept(finalResult);
            }
        }
    }
    
    /**
     * Adds an audio chunk to the processing queue.
     *
     * @param audioChunk the audio data to process
     */
    public void addAudioChunk(byte[] audioChunk) {
        if (!isRunning) {
            throw new IllegalStateException("Streaming is not active for session: " + sessionId);
        }
        
        try {
            audioQueue.put(audioChunk);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Interrupted while adding audio chunk for session: {}", sessionId);
        }
    }
    
    /**
     * Gets the latest transcription result.
     *
     * @return the latest transcription
     */
    public String getLatestTranscription() {
        return latestTranscription;
    }
    
    /**
     * Stops the streaming transcription.
     */
    public void stopStreaming() {
        isRunning = false;
        
        // Clear the queue to unblock any waiting threads
        audioQueue.clear();
        
        // Wait for the streaming to complete
        if (streamingFuture != null) {
            streamingFuture.join();
        }
    }
}
