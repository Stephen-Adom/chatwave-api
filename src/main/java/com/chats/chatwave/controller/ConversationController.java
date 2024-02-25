package com.chats.chatwave.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.chats.chatwave.model.Conversation;
import com.chats.chatwave.model.Dto.ConversationDto;
import com.chats.chatwave.model.RequestModel.ConversationRequest;
import com.chats.chatwave.service.ConversationService;

import graphql.GraphQLException;

@Controller
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @SchemaMapping(typeName = "Query", field = "allConversations")
    public List<Conversation> getAllConversations() {
        List<Conversation> allConversations = new ArrayList<>();

        return allConversations;
    }

    @SchemaMapping(typeName = "Mutation", field = "createConversation")
    public ConversationDto createConversation(@Argument("request") ConversationRequest request)
            throws GraphQLException {
        ConversationDto conversationDto = this.conversationService.createConversation(request);

        return conversationDto;
    }

    @SchemaMapping(typeName = "Query", field = "conversationById")
    public ConversationDto conversationById(@Argument("id") Long id)
            throws GraphQLException {

        return null;
    }

    @QueryMapping
    public List<ConversationDto> allUserConversations(@Argument("id") Long id) {
        List<ConversationDto> allConversationDto = this.conversationService.allUserConversations(id);
        return allConversationDto;
    }
}
