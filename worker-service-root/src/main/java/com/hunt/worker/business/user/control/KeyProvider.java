package com.hunt.worker-service-root.business.user.control;

import java.security.Key;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KeyProvider {

    @Value("${security.tokenKey}")
    String tokenKey;
    private Key key;

    @PostConstruct
    public void init() {
        log.info("Initiating KeyProvider for JWT.");
        key = new SecretKeySpec(tokenKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public Key getKey() {
        return key;
    }
}
