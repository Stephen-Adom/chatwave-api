package com.chats.chatwave.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityNotFoundException extends Exception {
    private HttpStatus status;
    private String message;

    public EntityNotFoundException(String message, HttpStatus status) {
        super();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
