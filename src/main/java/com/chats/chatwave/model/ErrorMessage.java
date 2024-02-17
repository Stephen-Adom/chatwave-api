package com.chats.chatwave.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorMessage {
    private HttpStatus status;
    private HttpStatusCode code;
    private String message;
}
