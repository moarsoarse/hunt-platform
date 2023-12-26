package com.hunt.bpm-launcher-service.business.user.boundary;

import com.hunt.bpm-launcher-service.BaseIntegrationTest;
import com.hunt.bpm-launcher-service.TestBuilderConfiguration;
import com.hunt.bpm-launcher-service.business.user.entity.UserTestBuilder;
import com.hunt.bpm-launcher-service.business.user.entity.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityFlowIT extends BaseIntegrationTest {

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWdlbnRvIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1MDkzOS9iMmIxNWFmMC02NGRlLTQzZjctODVlYy0wZjhjYThhODFlNmEvcnMvdXNlci9hdXRoZW50aWNhdGUiLCJpYXQiOjE0ODMyOTc4OTIsImV4cCI6MTQ4MzI5ODc5Mn0._yJvPJZnMXdPjg4UGX8xgELXIcbO7P2luH20RSyTXHwR1cpPt9hDJVZqCFDQUYbSfOtXWvZ5vu7dZ82hqPMy-Q";

    private static String token;

    @Autowired
    private UserTestBuilder userTestBuilder;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate.getRestTemplate().setInterceptors(Collections.emptyList());
    }

    /**
     * When secured request and no token, then UNAUTHORIZED.
     */
    @Test
    @Order(1)
    public void step01() {
        //GIVEN
        HttpEntity<String> httpEntity = new HttpEntity<>(EXPIRED_TOKEN);

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange( "/api/user/info", HttpMethod.POST, httpEntity, String.class);

        //THEN
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
    }

    /**
     * When login with correct login and password, then OK.
     */
    @Test
    @Order(2)
    public void step02() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername(UserTestBuilder.USERNAME_1);
        usernameAndPassword.setPassword(UserTestBuilder.PASSWORD_1);

        //WHEN
        ResponseEntity response = restTemplate.postForEntity("/api/user/login", usernameAndPassword, String.class);

        //THEN
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        token = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    /**
     * When secured and token, then OK.
     */
    @Test
    @Order(3)
    public void step03() {
        //GIVEN
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<String> httpEntity = new HttpEntity<>(token, httpHeaders);

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/api/user/info", HttpMethod.POST, httpEntity, String.class);

        //THEN
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }
}