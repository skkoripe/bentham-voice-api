package com.bentham.voiceagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the AWS credentials configuration class.
 */
@SpringBootTest
@ActiveProfiles("dev")
class AwsCredentialsConfigTest {

    @Autowired
    private AwsCredentialsProvider awsCredentialsProvider;

    @Test
    void credentialsProviderShouldBeCreated() {
        assertNotNull(awsCredentialsProvider, "AWS credentials provider should not be null");
    }
}
