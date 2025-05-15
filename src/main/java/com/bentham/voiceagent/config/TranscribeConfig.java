package com.bentham.voiceagent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration properties for Amazon Transcribe service.
 * This class holds configuration values specific to the Transcribe service.
 */
@Configuration
@PropertySource("classpath:application.yml")
public class TranscribeConfig {

    @Value("${aws.transcribe.language-code:en-US}")
    private String languageCode;

    @Value("${aws.transcribe.sample-rate:16000}")
    private Integer sampleRate;

    @Value("${aws.transcribe.enable-partial-results:true}")
    private Boolean enablePartialResults;

    /**
     * Gets the language code for transcription.
     *
     * @return the language code
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Gets the audio sample rate in Hz.
     *
     * @return the sample rate
     */
    public Integer getSampleRate() {
        return sampleRate;
    }

    /**
     * Checks if partial results are enabled.
     *
     * @return true if partial results are enabled, false otherwise
     */
    public Boolean getEnablePartialResults() {
        return enablePartialResults;
    }
}
