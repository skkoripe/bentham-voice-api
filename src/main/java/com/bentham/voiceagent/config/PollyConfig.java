package com.bentham.voiceagent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration properties for Amazon Polly service.
 * This class holds configuration values specific to the Polly service.
 */
@Configuration
@PropertySource("classpath:application.yml")
public class PollyConfig {

    @Value("${aws.polly.voice-id:Joanna}")
    private String voiceId;

    @Value("${aws.polly.output-format:mp3}")
    private String outputFormat;

    /**
     * Gets the voice ID for speech synthesis.
     *
     * @return the voice ID
     */
    public String getVoiceId() {
        return voiceId;
    }

    /**
     * Gets the output audio format.
     *
     * @return the output format
     */
    public String getOutputFormat() {
        return outputFormat;
    }
}
