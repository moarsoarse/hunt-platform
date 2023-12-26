package com.hunt.worker-service-root.configuration;

import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import com.hunt.worker-service-root.business.user.control.TokenValidator;
import com.hunt.worker-service-root.business.user.entity.AuthenticationException;
import com.hunt.worker-service-root.business.user.entity.TokenNotValidException;
import com.hunt.worker-service-root.business.user.entity.ValidatedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private TokenValidator tokenValidator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authorizationHeader = authentication.getName();

        try {
            ValidatedToken validatedToken = tokenValidator.validate(authorizationHeader);

            return new UsernamePasswordAuthenticationToken(validatedToken.getSubject(), null, Collections.emptyList());
        } catch (TokenNotValidException e) {
            log.warn("Token is invalid!", e);
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}



