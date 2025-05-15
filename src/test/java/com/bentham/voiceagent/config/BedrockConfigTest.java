package com.bentham.voiceagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the Bedrock configuration class.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "aws.bedrock.model-id=anthropic.claude-3-sonnet-20240229-v1:0",
        "aws.bedrock.temperature=0.7",
        "aws.bedrock.max-tokens=1024"
})
class BedrockConfigTest {

    @Autowired
    private BedrockConfig bedrockConfig;

    @Test
    void configShouldLoadModelId() {
        assertNotNull(bedrockConfig.getModelId(), "Model ID should not be null");
        assertEquals("anthropic.claude-3-sonnet-20240229-v1:0", bedrockConfig.getModelId(), 
                "Model ID should match configuration");
    }

    @Test
    void configShouldLoadTemperature() {
        assertNotNull(bedrockConfig.getTemperature(), "Temperature should not be null");
        assertEquals(0.7, bedrockConfig.getTemperature(), 0.001, 
                "Temperature should match configuration");
    }

    @Test
    void configShouldLoadMaxTokens() {
        assertNotNull(bedrockConfig.getMaxTokens(), "Max tokens should not be null");
        assertEquals(1024, bedrockConfig.getMaxTokens(), 
                "Max tokens should match configuration");
    }
}
