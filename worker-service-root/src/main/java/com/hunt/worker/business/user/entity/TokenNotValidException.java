package com.hunt.worker-service-root.business.user.entity;

public class TokenNotValidException extends Exception {

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}