package com.hunt.bpm-launcher-service.business.user.control;

import java.security.Key;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class KeyProviderTest {

    private static final String TOKEN_SEED = "tokenSeed";

    @InjectMocks
    private KeyProvider keyProvider;

    @Test
    public void init() {
        //GIVEN
        keyProvider.tokenKey = TOKEN_SEED;

        //WHEN
        keyProvider.init();

        //THEN
        Key key = keyProvider.getKey();

        assertNotNull(key);
    }
}