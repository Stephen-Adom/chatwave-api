package com.chats.chatwave.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chats.chatwave.model.Conversation;

@Controller
public class ConversationController {

    @SchemaMapping(typeName = "Query", field = "allConversations")
    public List<Conversation> getAllConversations() {
        List<Conversation> allConversations = new ArrayList<>();

        return allConversations;
    }
}
