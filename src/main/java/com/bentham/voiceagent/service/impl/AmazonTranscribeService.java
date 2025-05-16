package com.bentham.voiceagent.service.impl;

import com.bentham.voiceagent.config.TranscribeConfig;
import com.bentham.voiceagent.model.TranscriptionResult;
import com.bentham.voiceagent.service.TranscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Implementation of the TranscriptionService using Amazon Transcribe.
 * This service provides speech-to-text functionality using AWS Transcribe.
 */
@Service
public class AmazonTranscribeService implements TranscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(AmazonTranscribeService.class);
    
    private final TranscribeClient transcribeClient;
    private final TranscribeConfig transcribeConfig;
    private final AwsCredentialsProvider credentialsProvider;
    
    // Store for streaming sessions using StreamingTranscriptionHandler
    private final Map<String, StreamingTranscriptionHandler> streamingSessions = new ConcurrentHashMap<>();
    
    @Autowired
    public AmazonTranscribeService(TranscribeClient transcribeClient, 
                                  TranscribeConfig transcribeConfig,
                                  AwsCredentialsProvider credentialsProvider) {
        this.transcribeClient = transcribeClient;
        this.transcribeConfig = transcribeConfig;
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public CompletableFuture<String> transcribeAudio(InputStream audioStream) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Create a unique job name
                String jobName = "bentham-transcription-" + UUID.randomUUID().toString();
                
                // For a real implementation, we would upload the audio to S3 first
                // Here we'll use a pre-signed URL approach for simplicity
                
                // Read the audio data
                byte[] audioData = audioStream.readAllBytes();
                
                // Create a request to start a transcription job
                StartTranscriptionJobRequest request = StartTranscriptionJobRequest.builder()
                        .transcriptionJobName(jobName)
                        .languageCode(LanguageCode.fromValue(transcribeConfig.getLanguageCode()))
                        .mediaFormat(MediaFormat.WAV) // Adjust based on your audio format
                        .media(Media.builder()
                                .mediaFileUri("s3://example-bucket/example-audio.wav") // This would be a real S3 URI in production
                                .build())
                        .build();
                
                // Start the transcription job
                StartTranscriptionJobResponse response = transcribeClient.startTranscriptionJob(request);
                logger.info("Started transcription job: {}", response.transcriptionJob().transcriptionJobName());
                
                // Poll for job completion
                GetTranscriptionJobRequest getRequest = GetTranscriptionJobRequest.builder()
                        .transcriptionJobName(jobName)
                        .build();
                
                TranscriptionJob job;
                do {
                    // Wait before polling again
                    Thread.sleep(1000);
                    
                    // Get job status
                    job = transcribeClient.getTranscriptionJob(getRequest).transcriptionJob();
                    logger.debug("Job status: {}", job.transcriptionJobStatus());
                    
                } while (job.transcriptionJobStatus() == TranscriptionJobStatus.IN_PROGRESS);
                
                // Check if job completed successfully
                if (job.transcriptionJobStatus() == TranscriptionJobStatus.COMPLETED) {
                    // In a real implementation, you would download and parse the transcript
                    // For simplicity, we'll return a placeholder
                    return "Transcription completed. Result available at: " + job.transcript().transcriptFileUri();
                } else {
                    throw new RuntimeException("Transcription job failed with status: " + job.transcriptionJobStatus());
                }
                
            } catch (Exception e) {
                logger.error("Error during transcription", e);
                throw new RuntimeException("Failed to transcribe audio", e);
            }
        });
    }

    @Override
    public String startStreamingTranscription() {
        String sessionId = UUID.randomUUID().toString();
        
        // Create a new streaming handler with a consumer for transcription results
        StreamingTranscriptionHandler handler = new StreamingTranscriptionHandler(
            sessionId, 
            transcribeConfig,
            result -> {
                // Handle transcription results
                logger.info("Received transcription result: {}", result);
                // You might want to store this or notify clients
            }
        );
        
        // Start streaming
        handler.startStreaming();
        
        // Store the handler
        streamingSessions.put(sessionId, handler);
        
        logger.info("Started streaming transcription session: {}", sessionId);
        return sessionId;
    }

    @Override
    public void sendAudioChunk(String sessionId, byte[] audioChunk) {
        StreamingTranscriptionHandler handler = getHandler(sessionId);
        handler.addAudioChunk(audioChunk);
    }

    @Override
    public String getLatestTranscription(String sessionId) {
        StreamingTranscriptionHandler handler = getHandler(sessionId);
        return handler.getLatestTranscription();
    }

    @Override
    public String stopStreamingTranscription(String sessionId) {
        StreamingTranscriptionHandler handler = getHandler(sessionId);
        String finalTranscription = handler.getLatestTranscription();
        
        // Stop streaming
        handler.stopStreaming();
        
        // Remove the handler
        streamingSessions.remove(sessionId);
        logger.info("Stopped streaming transcription session: {}", sessionId);
        
        return finalTranscription;
    }
    
    private StreamingTranscriptionHandler getHandler(String sessionId) {
        StreamingTranscriptionHandler handler = streamingSessions.get(sessionId);
        if (handler == null) {
            throw new IllegalArgumentException("No active transcription session found with ID: " + sessionId);
        }
        return handler;
    }
}
