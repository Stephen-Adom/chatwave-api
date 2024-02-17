package com.chats.chatwave.model;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ValidationError {
    private HttpStatus status;
    private List<String> messages;
}
