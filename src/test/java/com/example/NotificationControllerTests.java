package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerTests extends TestContainersConfiguration {

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Test
    void message_should_return_status_ok_and_home_message() {
        ResponseEntity<List<String>> hello = testRestTemplate.exchange("/process",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {
                });
        assertThat(hello.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
