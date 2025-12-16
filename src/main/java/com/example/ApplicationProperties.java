package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public record ApplicationProperties(String region, String endpointUrl, String accessKey, String secretKey) {
}