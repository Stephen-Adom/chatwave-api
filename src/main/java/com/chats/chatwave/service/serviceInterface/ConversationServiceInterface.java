package com.chats.chatwave.service.serviceInterface;

import java.util.List;

import com.chats.chatwave.Exception.GraphqlEntityNotFound;
import com.chats.chatwave.Exception.GraphqlValidationErrorException;
import com.chats.chatwave.model.Dto.ConversationDto;
import com.chats.chatwave.model.RequestModel.ConversationRequest;

public interface ConversationServiceInterface {

    public ConversationDto createConversation(ConversationRequest request) throws GraphqlValidationErrorException;

    public List<ConversationDto> allUserConversations(Long userId) throws GraphqlEntityNotFound;
}
