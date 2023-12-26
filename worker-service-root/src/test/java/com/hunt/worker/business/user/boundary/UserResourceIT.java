package com.hunt.worker-service-root.business.user.boundary;

import java.security.Key;
import java.util.Collections;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import com.hunt.worker-service-root.BaseIntegrationTest;
import com.hunt.worker-service-root.TestBuilderConfiguration;
import com.hunt.worker-service-root.business.user.control.KeyProvider;
import com.hunt.worker-service-root.business.user.entity.User;
import com.hunt.worker-service-root.business.user.entity.UserTestBuilder;
import com.hunt.worker-service-root.business.user.entity.UsernameAndPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DirtiesContext
public class UserResourceIT extends BaseIntegrationTest {

    @Autowired
    private UserTestBuilder userTestBuilder;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private KeyProvider keyGenerator;

    @BeforeEach
    public void setUp() {
        restTemplate.getRestTemplate().setInterceptors(Collections.emptyList());
    }

    @Test
    public void loginGivenCorrectUsernameAndPassword() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername(UserTestBuilder.USERNAME_1);
        usernameAndPassword.setPassword(UserTestBuilder.PASSWORD_1);

        //WHEN
        ResponseEntity response = restTemplate.postForEntity("/api/user/login", usernameAndPassword, String.class);

        //THEN
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));

        String token = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        log.info("Token = {}.", token);

        //Validate token
        Key key = keyGenerator.getKey();
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

        assertEquals(1, claimsJws.getHeader().size());
        assertEquals("HS512", claimsJws.getHeader().getAlgorithm());
        assertEquals(4, claimsJws.getBody().size());
        assertEquals(UserTestBuilder.USERNAME_1, claimsJws.getBody().getSubject());
        assertEquals("issuer", claimsJws.getBody().getIssuer());
        assertNotNull(claimsJws.getBody().getIssuedAt());
        assertNotNull(claimsJws.getBody().getExpiration());
    }

    @Test
    public void loginGivenIncorrectUsername() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername("some funny username hahaha");
        usernameAndPassword.setPassword(UserTestBuilder.PASSWORD_1);

        //WHEN
        ResponseEntity response = restTemplate.postForEntity("/api/user/login", usernameAndPassword, String.class);

        //THEN
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
    }

    @Test
    public void loginGivenIncorrectPassword() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername(UserTestBuilder.USERNAME_1);
        usernameAndPassword.setPassword("incorrect passwordzik");

        //WHEN
        ResponseEntity response = restTemplate.postForEntity("/api/user/login", usernameAndPassword, String.class);

        //THEN
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
    }

    @Test
    public void infoGivenCorrectToken() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        String token = loginAndGetToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<String> httpEntity = new HttpEntity<>(token, httpHeaders);

        //WHEN
        ResponseEntity<User> response = restTemplate.exchange("/api/user/info", HttpMethod.POST, httpEntity, User.class);

        //THEN
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertEquals("user1", response.getBody().getUsername());
    }

    private String loginAndGetToken() {
        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername(UserTestBuilder.USERNAME_1);
        usernameAndPassword.setPassword(UserTestBuilder.PASSWORD_1);

        ResponseEntity loginResponse = restTemplate.postForEntity("/api/user/login", usernameAndPassword, String.class);
        return loginResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    @Test
    public void infoGivenIncorrectToken() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        String token = loginAndGetToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<String> httpEntity = new HttpEntity<>("some incorrect token", httpHeaders);

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/api/user/info", HttpMethod.POST, httpEntity, String.class);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }

    @Test
    public void infoWhenUserNotExists() {
        //GIVEN
        userTestBuilder.build(TestBuilderConfiguration.CLEARED_AND_SAVED);

        String token = loginAndGetToken();

        userTestBuilder.clear();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, token);

        HttpEntity<String> httpEntity = new HttpEntity<>(token, httpHeaders);

        //WHEN
        ResponseEntity<String> response = restTemplate.exchange("/api/user/info", HttpMethod.POST, httpEntity, String.class);

        //THEN
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }
}