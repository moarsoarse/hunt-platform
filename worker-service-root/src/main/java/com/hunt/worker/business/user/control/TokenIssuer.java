package com.hunt.worker-service-root.business.user.control;

import com.hunt.worker-service-root.business.user.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TokenIssuer {

    @Value("${security.expirationTimeInMinutes}")
    Integer expirationTimeInMinutes;

    @Value("${security.issuer}")
    String issuer;

    @Autowired
    private KeyProvider keyProvider;

    public String issueToken(User user) {
        Key key = keyProvider.getKey();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expirationDateTime = currentDateTime.plusMinutes(user.getExpirationTimeInMinutes());

        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuer(issuer)
            .setIssuedAt(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, key)
             .compact();
    }
}
