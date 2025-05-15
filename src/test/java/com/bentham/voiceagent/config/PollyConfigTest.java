package com.bentham.voiceagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the Polly configuration class.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "aws.polly.voice-id=Joanna",
        "aws.polly.output-format=mp3"
})
class PollyConfigTest {

    @Autowired
    private PollyConfig pollyConfig;

    @Test
    void configShouldLoadVoiceId() {
        assertNotNull(pollyConfig.getVoiceId(), "Voice ID should not be null");
        assertEquals("Joanna", pollyConfig.getVoiceId(), "Voice ID should match configuration");
    }

    @Test
    void configShouldLoadOutputFormat() {
        assertNotNull(pollyConfig.getOutputFormat(), "Output format should not be null");
        assertEquals("mp3", pollyConfig.getOutputFormat(), "Output format should match configuration");
    }
}
