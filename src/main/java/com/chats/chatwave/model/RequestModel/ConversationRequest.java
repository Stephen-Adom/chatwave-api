package com.chats.chatwave.model.RequestModel;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConversationRequest {
    @NotEmpty(message = "User 1 id is required")
    private Long user1;

    @NotEmpty(message = "User 2 id is required")
    private Long user2;
}
