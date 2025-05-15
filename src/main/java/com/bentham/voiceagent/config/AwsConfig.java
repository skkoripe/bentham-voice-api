package com.bentham.voiceagent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

/**
 * Configuration class for AWS services.
 * This class provides beans for AWS service clients used in the application.
 */
@Configuration
public class AwsConfig {

    @Value("${aws.region:us-west-2}")
    private String awsRegion;

    /**
     * Creates a TranscribeClient bean for Amazon Transcribe service.
     *
     * @return configured TranscribeClient
     */
    @Bean
    public TranscribeClient transcribeClient() {
        return TranscribeClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    /**
     * Creates a PollyClient bean for Amazon Polly service.
     *
     * @return configured PollyClient
     */
    @Bean
    public PollyClient pollyClient() {
        return PollyClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    
    /**
     * Creates an S3Client bean for Amazon S3 service.
     *
     * @return configured S3Client
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    
    /**
     * Creates a CloudWatchClient bean for Amazon CloudWatch service.
     *
     * @return configured CloudWatchClient
     */
    @Bean
    public CloudWatchClient cloudWatchClient() {
        return CloudWatchClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
