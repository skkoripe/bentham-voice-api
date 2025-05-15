package com.bentham.voiceagent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration properties for Amazon Bedrock service.
 * This class holds configuration values specific to the Bedrock service.
 */
@Configuration
@PropertySource("classpath:application.yml")
public class BedrockConfig {

    @Value("${aws.bedrock.model-id:anthropic.claude-3-sonnet-20240229-v1:0}")
    private String modelId;

    @Value("${aws.bedrock.temperature:0.7}")
    private Double temperature;

    @Value("${aws.bedrock.max-tokens:1024}")
    private Integer maxTokens;

    /**
     * Gets the model ID for the Bedrock service.
     *
     * @return the model ID
     */
    public String getModelId() {
        return modelId;
    }

    /**
     * Gets the temperature parameter for model inference.
     *
     * @return the temperature value
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * Gets the maximum number of tokens for model responses.
     *
     * @return the maximum tokens
     */
    public Integer getMaxTokens() {
        return maxTokens;
    }
}
