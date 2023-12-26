package com.hunt.worker-service-root.configuration;

import com.hunt.worker-service-root.business.user.control.TokenValidator;
import com.hunt.worker-service-root.business.user.entity.TokenNotValidException;
import com.hunt.worker-service-root.business.user.entity.ValidatedToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationProviderTest {

    @Mock
    private Authentication authentication;

    @Mock
    private TokenValidator tokenValidator;

    @InjectMocks
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Test
    public void authenticateWhenInvalidToken() throws TokenNotValidException {
        //GIVEN
        String token = "token";
        doReturn(token).when(authentication).getName();
        doThrow(TokenNotValidException.class).when(tokenValidator).validate(token);

        //WHEN
        Authentication result = tokenAuthenticationProvider.authenticate(authentication);

        //THEN
        assertNull(result);
    }

    @Test
    public void authenticateWhenValidToken() throws TokenNotValidException {
        //GIVEN
        String token = "token";
        String subject = "subject";
        doReturn(token).when(authentication).getName();
        doReturn(ValidatedToken.builder().subject(subject).token(token).build()).when(tokenValidator).validate(token);

        //WHEN
        Authentication result = tokenAuthenticationProvider.authenticate(authentication);

        //THEN
        assertNotNull(result);
        assertEquals(subject, result.getPrincipal());
    }

    @Test
    public void supports() {
        //WHEN
        boolean supports = tokenAuthenticationProvider.supports(PreAuthenticatedAuthenticationToken.class);

        //THEN
        assertTrue(supports);
    }
}