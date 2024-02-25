package com.chats.chatwave.service.serviceInterface;

import com.chats.chatwave.Exception.GraphqlEntityNotFound;
import com.chats.chatwave.model.Dto.MessageDto;

public interface MessageServiceInterface {
    public MessageDto addMessageToConversation(String message, Long conversationId, Long sender)
            throws GraphqlEntityNotFound;
}
