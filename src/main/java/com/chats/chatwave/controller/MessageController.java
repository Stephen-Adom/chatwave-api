package com.chats.chatwave.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chats.chatwave.model.Dto.MessageDto;
import com.chats.chatwave.service.MessageService;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @SchemaMapping(typeName = "Mutation", field = "addMessageToConversation")
    public MessageDto addMessageToConversation(@Argument("message") String message,
            @Argument("conversationId") Long conversationId, @Argument("sender") Long sender) {
        MessageDto messageDto = this.messageService.addMessageToConversation(message, conversationId, sender);

        return messageDto;
    }
}
