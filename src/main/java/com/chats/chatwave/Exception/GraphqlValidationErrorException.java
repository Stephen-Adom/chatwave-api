package com.chats.chatwave.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphqlValidationErrorException extends RuntimeException {
    public String message;

    public GraphqlValidationErrorException(String message) {
        this.message = message;
    }
}
