package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ImportTestcontainers(TestContainersConfiguration.class)
//@Testcontainers
class AwsCognitoTests extends TestContainersConfiguration {

    @Autowired
    CognitoIdentityProviderClient cognitoClient;

    @Test
    void test_cognito() {
        var userPoolResponse = cognitoClient
                .createUserPool(CreateUserPoolRequest.builder().poolName("awspring-test").build());
        var userPoolId = userPoolResponse.userPool().id();

        var userPoolClientResponse = cognitoClient.createUserPoolClient(CreateUserPoolClientRequest.builder()
                .clientName("awspring-test-client")
                .userPoolId(userPoolId)
                .build());
        var appClientId = userPoolClientResponse.userPoolClient().clientId();

        cognitoClient.adminCreateUser(AdminCreateUserRequest.builder()
                .userPoolId(userPoolId)
                .username("testuser@test.com")
                .temporaryPassword("testP@ssw0rd")
                .userAttributes(AttributeType.builder().name("email").value("testuser@test.com").build(),
                        AttributeType.builder().name("email_verified").value("true").build())
                .build());

        cognitoClient.adminResetUserPassword(AdminResetUserPasswordRequest.builder()
                .username("testuser@test.com")
                .userPoolId(userPoolId)
                .build());

        cognitoClient.adminSetUserPassword(AdminSetUserPasswordRequest.builder()
                .username("testuser@test.com")
                .password("testP4ssw*rd")
                .userPoolId(userPoolId)
                .permanent(Boolean.TRUE)
                .build());

        var initiateAuthResponse = cognitoClient.initiateAuth(InitiateAuthRequest.builder()
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .clientId(appClientId)
                .authParameters(Map.of("USERNAME", "testuser@test.com", "PASSWORD", "testP4ssw*rd"))
                .build());
        var accessToken = initiateAuthResponse.authenticationResult().accessToken();
        assertThat(accessToken).isNotNull();
    }

}