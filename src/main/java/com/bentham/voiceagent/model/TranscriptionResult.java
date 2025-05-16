package com.bentham.voiceagent.model;

import java.time.Instant;

/**
 * Model class representing a transcription result.
 * This class holds the result of a speech-to-text transcription.
 */
public class TranscriptionResult {

    private final String sessionId;
    private final String transcript;
    private final boolean isPartial;
    private final double confidence;
    private final Instant timestamp;
    
    /**
     * Creates a new transcription result.
     *
     * @param sessionId the ID of the transcription session
     * @param transcript the transcribed text
     * @param isPartial whether this is a partial result
     * @param confidence the confidence score (0.0 to 1.0)
     */
    public TranscriptionResult(String sessionId, String transcript, boolean isPartial, double confidence) {
        this.sessionId = sessionId;
        this.transcript = transcript;
        this.isPartial = isPartial;
        this.confidence = confidence;
        this.timestamp = Instant.now();
    }
    
    /**
     * Creates a new partial transcription result.
     *
     * @param sessionId the ID of the transcription session
     * @param transcript the transcribed text
     * @return a new partial transcription result
     */
    public static TranscriptionResult partial(String sessionId, String transcript) {
        return new TranscriptionResult(sessionId, transcript, true, 0.0);
    }
    
    /**
     * Creates a new final transcription result.
     *
     * @param sessionId the ID of the transcription session
     * @param transcript the transcribed text
     * @param confidence the confidence score (0.0 to 1.0)
     * @return a new final transcription result
     */
    public static TranscriptionResult final_(String sessionId, String transcript, double confidence) {
        return new TranscriptionResult(sessionId, transcript, false, confidence);
    }
    
    /**
     * Gets the ID of the transcription session.
     *
     * @return the session ID
     */
    public String getSessionId() {
        return sessionId;
    }
    
    /**
     * Gets the transcribed text.
     *
     * @return the transcript
     */
    public String getTranscript() {
        return transcript;
    }
    
    /**
     * Checks if this is a partial result.
     *
     * @return true if partial, false if final
     */
    public boolean isPartial() {
        return isPartial;
    }
    
    /**
     * Gets the confidence score.
     *
     * @return the confidence score (0.0 to 1.0)
     */
    public double getConfidence() {
        return confidence;
    }
    
    /**
     * Gets the timestamp when this result was created.
     *
     * @return the timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "TranscriptionResult{" +
                "sessionId='" + sessionId + '\'' +
                ", transcript='" + transcript + '\'' +
                ", isPartial=" + isPartial +
                ", confidence=" + confidence +
                ", timestamp=" + timestamp +
                '}';
    }
}
