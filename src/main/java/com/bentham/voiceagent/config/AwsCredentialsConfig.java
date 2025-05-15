package com.bentham.voiceagent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

/**
 * Configuration for AWS credentials.
 * This class provides different credential providers based on the active profile.
 */
@Configuration
public class AwsCredentialsConfig {

    @Value("${aws.profile:default}")
    private String awsProfile;

    /**
     * Creates an AWS credentials provider for development environment.
     * Uses profile credentials from ~/.aws/credentials.
     *
     * @return the AWS credentials provider
     */
    @Bean
    @Profile("dev")
    public AwsCredentialsProvider devCredentialsProvider() {
        return ProfileCredentialsProvider.create(awsProfile);
    }

    /**
     * Creates an AWS credentials provider for production environment.
     * Uses default credentials provider chain which includes environment variables,
     * system properties, credential profiles, and IAM roles.
     *
     * @return the AWS credentials provider
     */
    @Bean
    @Profile("prod")
    public AwsCredentialsProvider prodCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    /**
     * Creates a default AWS credentials provider for any other environment.
     *
     * @return the AWS credentials provider
     */
    @Bean
    @Profile("!dev & !prod")
    public AwsCredentialsProvider defaultCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }
}
