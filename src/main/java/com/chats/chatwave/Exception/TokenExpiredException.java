package com.chats.chatwave.Exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends RuntimeException {
    private String message;
    private HttpStatus status;

    public TokenExpiredException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
