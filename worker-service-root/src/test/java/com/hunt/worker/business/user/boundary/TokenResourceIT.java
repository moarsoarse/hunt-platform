package com.hunt.worker-service-root.business.user.boundary;

import com.hunt.worker-service-root.BaseIntegrationTest;
import com.hunt.worker-service-root.TestBuilderConfiguration;
import com.hunt.worker-service-root.business.user.entity.UserTestBuilder;
import com.hunt.worker-service-root.business.user.entity.UsernameAndPassword;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenResourceIT extends BaseIntegrationTest {

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWdlbnRvIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1MDkzOS9iMmIxNWFmMC02NGRlLTQzZjctODVlYy0wZjhjYThhODFlNmEvcnMvdXNlci9hdXRoZW50aWNhdGUiLCJpYXQiOjE0ODMyOTc4OTIsImV4cCI6MTQ4MzI5ODc5Mn0._yJvPJZnMXdPjg4UGX8xgELXIcbO7P2luH20RSyTXHwR1cpPt9hDJVZqCFDQUYbSfOtXWvZ5vu7dZ82hqPMy-Q";

    @Autowired
    private UserTestBuilder userTestBuilder;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void validateGivenValidToken() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        String token = loginAndGetToken();

        //WHEN
        ResponseEntity response = restTemplate.postForEntity("/api/token/validate", token, String.class);

        //THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private String loginAndGetToken() {
        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername(UserTestBuilder.USERNAME_1);
        usernameAndPassword.setPassword(UserTestBuilder.PASSWORD_1);

        ResponseEntity loginResponse = restTemplate.postForEntity("/api/user/login", usernameAndPassword, String.class);
        return loginResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    @Test
    public void validateGivenExpiredToken() {
        //GIVEN

        //WHEN
        ResponseEntity response = restTemplate.postForEntity("/api/token/validate", EXPIRED_TOKEN, String.class);

        //THEN
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}