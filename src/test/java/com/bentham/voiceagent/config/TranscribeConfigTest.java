package com.bentham.voiceagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the Transcribe configuration class.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "aws.transcribe.language-code=en-US",
        "aws.transcribe.sample-rate=16000",
        "aws.transcribe.enable-partial-results=true"
})
class TranscribeConfigTest {

    @Autowired
    private TranscribeConfig transcribeConfig;

    @Test
    void configShouldLoadLanguageCode() {
        assertNotNull(transcribeConfig.getLanguageCode(), "Language code should not be null");
        assertEquals("en-US", transcribeConfig.getLanguageCode(), "Language code should match configuration");
    }

    @Test
    void configShouldLoadSampleRate() {
        assertNotNull(transcribeConfig.getSampleRate(), "Sample rate should not be null");
        assertEquals(16000, transcribeConfig.getSampleRate(), "Sample rate should match configuration");
    }

    @Test
    void configShouldLoadPartialResultsFlag() {
        assertNotNull(transcribeConfig.getEnablePartialResults(), "Enable partial results flag should not be null");
        assertTrue(transcribeConfig.getEnablePartialResults(), "Enable partial results should be true");
    }
}
