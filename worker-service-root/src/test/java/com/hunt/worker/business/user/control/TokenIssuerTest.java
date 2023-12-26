package com.hunt.worker-service-root.business.user.control;

import com.hunt.worker-service-root.business.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TokenIssuerTest {

    public static final String ISSUER = "issuer";
    public static final int EXPIRATION_TIME_IN_MINUTES = 60;
    private static final String TOKEN_SEED = "123";
    @Mock
    private KeyProvider keyProvider;

    @InjectMocks
    private TokenIssuer tokenIssuer;

    @BeforeEach
    public void setUp() {
        doReturn(new SecretKeySpec(TOKEN_SEED.getBytes(), SignatureAlgorithm.HS256.getJcaName())).when(keyProvider).getKey();

        tokenIssuer.issuer = ISSUER;
        tokenIssuer.expirationTimeInMinutes = EXPIRATION_TIME_IN_MINUTES;
    }

    @Test
    public void issueToken() {
        //GIVEN
        User username = new User();
        username.setUsername("username");
        username.setExpirationTimeInMinutes(60L);

        //WHEN
        String token = tokenIssuer.issueToken(username);

        Key key = keyProvider.getKey();
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

        assertEquals(1, claimsJws.getHeader().size());
        assertEquals("HS512", claimsJws.getHeader().getAlgorithm());
        assertEquals(4, claimsJws.getBody().size());
        assertEquals(username.getUsername(), claimsJws.getBody().getSubject());
        assertEquals(ISSUER, claimsJws.getBody().getIssuer());
        assertNotNull(claimsJws.getBody().getIssuedAt());
        assertNotNull(claimsJws.getBody().getExpiration());
    }
}