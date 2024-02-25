package com.chats.chatwave.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphqlEntityNotFound extends RuntimeException {
    public String message;

    public GraphqlEntityNotFound(String message) {
        this.message = message;
    }
}
