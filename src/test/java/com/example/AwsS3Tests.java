package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AwsS3Tests extends TestContainersConfiguration {

    @Autowired
    S3Client s3Client;

    @Test
    void contextLoads() {
        List<Bucket> buckets = this.s3Client.listBuckets().buckets();
        assertThat(buckets).hasSize(0);
    }

}
