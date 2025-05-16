package com.bentham.voiceagent.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the TranscriptionResult class.
 */
class TranscriptionResultTest {

    @Test
    void partialShouldCreatePartialResult() {
        // Act
        TranscriptionResult result = TranscriptionResult.partial("test-session", "Test transcript");
        
        // Assert
        assertEquals("test-session", result.getSessionId());
        assertEquals("Test transcript", result.getTranscript());
        assertTrue(result.isPartial());
        assertEquals(0.0, result.getConfidence());
        assertNotNull(result.getTimestamp());
    }
    
    @Test
    void finalShouldCreateFinalResult() {
        // Act
        TranscriptionResult result = TranscriptionResult.final_("test-session", "Test transcript", 0.95);
        
        // Assert
        assertEquals("test-session", result.getSessionId());
        assertEquals("Test transcript", result.getTranscript());
        assertFalse(result.isPartial());
        assertEquals(0.95, result.getConfidence());
        assertNotNull(result.getTimestamp());
    }
    
    @Test
    void constructorShouldSetAllFields() {
        // Act
        TranscriptionResult result = new TranscriptionResult("test-session", "Test transcript", true, 0.8);
        
        // Assert
        assertEquals("test-session", result.getSessionId());
        assertEquals("Test transcript", result.getTranscript());
        assertTrue(result.isPartial());
        assertEquals(0.8, result.getConfidence());
        assertNotNull(result.getTimestamp());
    }
    
    @Test
    void toStringShouldIncludeAllFields() {
        // Arrange
        TranscriptionResult result = new TranscriptionResult("test-session", "Test transcript", false, 0.9);
        
        // Act
        String toString = result.toString();
        
        // Assert
        assertTrue(toString.contains("test-session"));
        assertTrue(toString.contains("Test transcript"));
        assertTrue(toString.contains("isPartial=false"));
        assertTrue(toString.contains("confidence=0.9"));
        assertTrue(toString.contains("timestamp="));
    }
}
