package com.bentham.voiceagent.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the AWS configuration class.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "aws.region=us-west-2"
})
class AwsConfigTest {

    @Autowired
    private TranscribeClient transcribeClient;

    @Autowired
    private PollyClient pollyClient;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private CloudWatchClient cloudWatchClient;

    @Test
    void transcribeClientShouldBeCreated() {
        assertNotNull(transcribeClient, "TranscribeClient should not be null");
    }

    @Test
    void pollyClientShouldBeCreated() {
        assertNotNull(pollyClient, "PollyClient should not be null");
    }

    @Test
    void s3ClientShouldBeCreated() {
        assertNotNull(s3Client, "S3Client should not be null");
    }

    @Test
    void cloudWatchClientShouldBeCreated() {
        assertNotNull(cloudWatchClient, "CloudWatchClient should not be null");
    }
}
