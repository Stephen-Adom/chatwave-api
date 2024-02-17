package com.chats.chatwave.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorMessage {
    private HttpStatus status;
    private String message;
}
