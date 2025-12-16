package com.example;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.VerifyEmailAddressRequest;
import software.amazon.awssdk.services.ses.model.VerifyEmailAddressResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;
import software.amazon.awssdk.services.sqs.SqsClient;

class TestContainersConfiguration {

    private static final String LOCALSTACK_IMAGE = "localstack/localstack-pro:4.12.0";
    private static final String MAIL_PIT_IMAGE = "axllent/mailpit";

    //@Container
    static final LocalStackContainer LOCALSTACK_CONTAINER;
    static final GenericContainer<?> MAIL_PIT_CONTAINER;


    static {
        MAIL_PIT_CONTAINER = new GenericContainer<>(DockerImageName.parse(MAIL_PIT_IMAGE)).withExposedPorts(1025, 8025);
        MAIL_PIT_CONTAINER.start();
        String smtpHost = MAIL_PIT_CONTAINER.getHost() + ":" + MAIL_PIT_CONTAINER.getMappedPort(1025);
        LOCALSTACK_CONTAINER = new LocalStackContainer(
                DockerImageName.parse(LOCALSTACK_IMAGE))
                .withEnv("LOCALSTACK_AUTH_TOKEN", "ls-VunEHuji-jizU-GOVE-jEPe-3371CarOcf6e")
                .withEnv("SMTP_HOST", smtpHost);
        LOCALSTACK_CONTAINER.start();

        String notificationQueue = "email-notification-queue";
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.of(LOCALSTACK_CONTAINER.getRegion()))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(LOCALSTACK_CONTAINER.getAccessKey(), LOCALSTACK_CONTAINER.getSecretKey())))
                .endpointOverride(LOCALSTACK_CONTAINER.getEndpoint())
                .build();
        var queue = sqsClient.createQueue(builder -> builder.queueName(notificationQueue));
        var queueAttributes = sqsClient.getQueueAttributes(
                builder -> builder.queueUrl(queue.queueUrl()).attributeNamesWithStrings("QueueArn"));
        var queueArn = queueAttributes.attributesAsStrings().get("QueueArn");
        System.out.println(queueArn);

        SnsClient snsClient = SnsClient.builder()
                .region(Region.of(LOCALSTACK_CONTAINER.getRegion()))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(LOCALSTACK_CONTAINER.getAccessKey(), LOCALSTACK_CONTAINER.getSecretKey())))
                .endpointOverride(LOCALSTACK_CONTAINER.getEndpoint())
                .build();
        CreateTopicResponse topic = snsClient.createTopic(CreateTopicRequest.builder().name("email-notifications").build());
        String topicArn = topic.topicArn();
        System.out.println(topicArn);
        SubscribeResponse subscribe = snsClient.subscribe(SubscribeRequest.builder().topicArn(topicArn).endpoint(queueArn).protocol("sqs").build());
        String subscriptionArn = subscribe.subscriptionArn();
        System.out.println(subscriptionArn);

        SesClient sesClient = SesClient.builder()
                .region(Region.of(LOCALSTACK_CONTAINER.getRegion()))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(LOCALSTACK_CONTAINER.getAccessKey(), LOCALSTACK_CONTAINER.getSecretKey())))
                .endpointOverride(LOCALSTACK_CONTAINER.getEndpoint())
                .build();
        VerifyEmailAddressResponse verifyEmailAddressResponse = sesClient.verifyEmailAddress(VerifyEmailAddressRequest.builder().emailAddress("no-reply@localstack.cloud").build());
        System.out.println(verifyEmailAddressResponse);

        PublishResponse publishResponse = snsClient.publish(PublishRequest.builder().subject("hello").message("{\"subject\":\"hello\", \"address\": \"alice@example.com\", \"body\": \"hello world\"}").topicArn(topicArn).build());
        System.out.println(publishResponse);

        sqsClient.close();
        snsClient.close();
        sesClient.close();
    }

    @DynamicPropertySource
    static void envProperties(DynamicPropertyRegistry registry) {
        registry.add("aws.region", LOCALSTACK_CONTAINER::getRegion);
        registry.add("aws.endpoint-url", LOCALSTACK_CONTAINER::getEndpoint);
        registry.add("aws.access-key", LOCALSTACK_CONTAINER::getAccessKey);
        registry.add("aws.secret-key", LOCALSTACK_CONTAINER::getSecretKey);
    }

}
