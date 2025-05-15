package com.bentham.voiceagent.service;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for speech-to-text transcription.
 * This interface defines methods for transcribing audio to text.
 */
public interface TranscriptionService {

    /**
     * Transcribes audio from an input stream.
     *
     * @param audioStream the audio input stream
     * @return a CompletableFuture that will be completed with the transcription result
     */
    CompletableFuture<String> transcribeAudio(InputStream audioStream);

    /**
     * Starts a streaming transcription session.
     *
     * @return a session ID for the streaming transcription
     */
    String startStreamingTranscription();

    /**
     * Sends audio data to an ongoing streaming transcription session.
     *
     * @param sessionId the session ID
     * @param audioChunk the audio data chunk
     */
    void sendAudioChunk(String sessionId, byte[] audioChunk);

    /**
     * Gets the latest transcription result from a streaming session.
     *
     * @param sessionId the session ID
     * @return the latest transcription result
     */
    String getLatestTranscription(String sessionId);

    /**
     * Stops a streaming transcription session.
     *
     * @param sessionId the session ID
     * @return the final transcription result
     */
    String stopStreamingTranscription(String sessionId);
}
