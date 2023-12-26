package com.hunt.worker-service-root.business.user.control;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.hunt.worker-service-root.business.user.entity.TokenNotValidException;
import com.hunt.worker-service-root.business.user.entity.ValidatedToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TokenValidatorTest {
    @Mock
    private KeyProvider keyProvider;

    private Key key = new SecretKeySpec("dsdasdasda".getBytes(), SignatureAlgorithm.HS256.getJcaName());

    @InjectMocks
    private TokenValidator tokenValidator;

    @BeforeEach
    public void setUp() {
        doReturn(key).when(keyProvider).getKey();
    }

    @Test
    public void authenticateWhenMalmformedToken() throws TokenNotValidException {
        //GIVEN

        //WHEN
        TokenNotValidException exception = assertThrows(TokenNotValidException.class, () -> {
            tokenValidator.validate("token");
        });

        //THEN
    }

    @Test
    public void authenticateWhenNullToken() throws TokenNotValidException {
        //GIVEN

        //WHEN
        TokenNotValidException exception = assertThrows(TokenNotValidException.class, () -> {
            tokenValidator.validate(null);
        });

        //THEN
        assertTrue(exception.getMessage().startsWith("Provided JSON Web Token = null is null, whitespace or empty, error message = JWT String argument cannot be null or empty."));
    }

    @Test
    public void authenticateWhenWhitespaceToken() throws TokenNotValidException {
        //GIVEN

        //WHEN
        TokenNotValidException exception = assertThrows(TokenNotValidException.class, () -> {
            tokenValidator.validate("     ");
        });

        //THEN
        assertTrue(exception.getMessage().startsWith("Provided JSON Web Token =  is null, whitespace or empty, error message = JWT String argument cannot be null or empty."));
    }

    @Test
    public void authenticateWhenEmptyToken() throws TokenNotValidException {
        //GIVEN

        //WHEN
        TokenNotValidException exception = assertThrows(TokenNotValidException.class, () -> {
            tokenValidator.validate("");
        });

        //THEN
        assertTrue(exception.getMessage().startsWith("Provided JSON Web Token =  is null, whitespace or empty, error message = JWT String argument cannot be null or empty."));
    }

    @Test
    public void authenticateWhenWrongSignature() throws TokenNotValidException {
        //GIVEN

        //WHEN
        TokenNotValidException exception = assertThrows(TokenNotValidException.class, () -> {
            tokenValidator.validate("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYWdlbnRvIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1MDkzOS9iMmIxNWFmMC02NGRlLTQzZjctODVlYy0wZjhjYThhODFlNmEvcnMvdXNlci9hdXRoZW50aWNhdGUiLCJpYXQiOjE0ODMyOTc4OTIsImV4cCI6MTQ4MzI5ODc5Mn0._yJvPJZnMXdPjg4UGX8xgELXIcbO7P2luH20RSyTXHwR1cpPt9hDJVZqCFDQUYbSfOtXWvZ5vu7dZ82hqPMy-Q");
        });

        //THEN
        assertTrue(exception.getMessage().startsWith("Signature validation of provided JSON Web Token =  has been failed, error message = JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."));
    }

    @Test
    public void authenticateWhenExpiredToken() throws TokenNotValidException {
        //GIVEN

        //WHEN
        TokenNotValidException exception = assertThrows(TokenNotValidException.class, () -> {
            tokenValidator.validate("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaXNzIjoibHBwIiwiaWF0IjoxNTM4MTQzODU3LCJleHAiOjE1MzgxNDM5MTd9.EEp7Q6Duocwum5asGTcU1XS0da5V-FjM22uxgMWGp8Pl4y4S5KkCzITw0ccUKi4gitSZhx5H2ZA6uc2opp1-3g");
        });

        //THEN
        assertTrue(exception.getMessage().startsWith("Provided JSON Web Token =  is expired, error message = JWT expired at 2018-09-28T16:11:57Z. Current time: "));
    }

    @Test
    public void authenticateWhenValidToken() throws TokenNotValidException {
        //GIVEN
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuer("issuer")
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        //WHEN
        ValidatedToken result = tokenValidator.validate(token);

        //THEN
        assertNotNull(result);
    }
}