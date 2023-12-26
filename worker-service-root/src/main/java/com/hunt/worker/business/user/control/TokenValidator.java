package com.hunt.worker-service-root.business.user.control;

import java.security.Key;
import java.text.MessageFormat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import com.hunt.worker-service-root.business.user.entity.TokenNotValidException;
import com.hunt.worker-service-root.business.user.entity.ValidatedToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenValidator {

    @Autowired
    private KeyProvider keyProvider;

    public ValidatedToken validate(String token) throws TokenNotValidException {
        try {
            Key key = keyProvider.getKey();

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            log.info("Token = {} is valid.", getTokenForLog(token));

            return ValidatedToken.builder().token(token).subject(claimsJws.getBody().getSubject()).build();
        } catch (UnsupportedJwtException e) {
            throw new TokenNotValidException(MessageFormat.format("Provided JSON Web Token = {0} doesn't represent supported Claims JWT, error message = {1}.", getTokenForLog(token), e.getMessage()), e);
        } catch (MalformedJwtException e) {
            throw new TokenNotValidException(MessageFormat.format("Provided JSON Web Token = {0} is invalid or has invalid format, error message = {1}.", getTokenForLog(token), e.getMessage()), e);
        } catch(SignatureException e) {
            throw new TokenNotValidException(MessageFormat.format("Signature validation of provided JSON Web Token = {0} has been failed, error message = {1}.", getTokenForLog(token), e.getMessage()), e);
        } catch (ExpiredJwtException e) {
            throw new TokenNotValidException(MessageFormat.format("Provided JSON Web Token = {0} is expired, error message = {1}.", getTokenForLog(token), e.getMessage()), e);
        } catch (IllegalArgumentException e) {
            throw new TokenNotValidException(MessageFormat.format("Provided JSON Web Token = {0} is null, whitespace or empty, error message = {1}.", getTokenForLog(token), e.getMessage()), e);
        } catch (Exception e) {
            throw new TokenNotValidException(MessageFormat.format("Unable to authenticate user with provided JSON Web Token = {0}, unknown exception occur, error message = {1}.", getTokenForLog(token), e.getMessage()), e);
        }
    }

    private String getTokenForLog(String token) {
        return StringUtils.substringAfterLast(token, "");
    }
}