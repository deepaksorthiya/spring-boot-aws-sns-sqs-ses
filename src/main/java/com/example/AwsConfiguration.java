package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class AwsConfiguration {

    private final ApplicationProperties applicationProperties;

    public AwsConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.of(applicationProperties.region()))
                .endpointOverride(URI.create(applicationProperties.endpointUrl()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(applicationProperties.accessKey(), applicationProperties.secretKey())

                        ))
                .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.of(applicationProperties.region()))
                .endpointOverride(URI.create(applicationProperties.endpointUrl()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(applicationProperties.accessKey(), applicationProperties.secretKey())

                        ))
                .build();
    }

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.of(applicationProperties.region()))
                .endpointOverride(URI.create(applicationProperties.endpointUrl()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(applicationProperties.accessKey(), applicationProperties.secretKey())

                        ))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(applicationProperties.region()))
                .endpointOverride(URI.create(applicationProperties.endpointUrl()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(applicationProperties.accessKey(), applicationProperties.secretKey())

                        ))
                .build();
    }

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(applicationProperties.region()))
                .endpointOverride(URI.create(applicationProperties.endpointUrl()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(applicationProperties.accessKey(), applicationProperties.secretKey())

                        ))
                .build();
    }

    @Bean
    public String notificationQueueUrl(SqsClient sqsClient) {
        return sqsClient.getQueueUrl(builder -> {
            // TODO: could get QueueUrl from stack output properties
            builder.queueName("email-notification-queue");
        }).queueUrl();
    }
}
