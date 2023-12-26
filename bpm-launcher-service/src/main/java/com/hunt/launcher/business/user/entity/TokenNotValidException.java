package com.hunt.bpm-launcher-service.business.user.entity;

public class TokenNotValidException extends Exception {

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}