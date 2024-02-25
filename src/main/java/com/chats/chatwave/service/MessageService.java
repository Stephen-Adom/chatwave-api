package com.chats.chatwave.service;

import org.springframework.stereotype.Service;

import com.chats.chatwave.Exception.GraphqlEntityNotFound;
import com.chats.chatwave.model.Conversation;
import com.chats.chatwave.model.Message;
import com.chats.chatwave.model.MessageStatus;
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.Dto.ConversationInfo;
import com.chats.chatwave.model.Dto.MessageDto;
import com.chats.chatwave.model.Dto.UserSummaryDto;
import com.chats.chatwave.repository.ConversationRepository;
import com.chats.chatwave.repository.MessageRepository;
import com.chats.chatwave.repository.UserRepository;
import com.chats.chatwave.service.serviceInterface.MessageServiceInterface;

@Service
public class MessageService implements MessageServiceInterface {

        private final UserRepository userRepository;

        private final ConversationRepository conversationRepository;

        private final MessageRepository messageRepository;

        public MessageService(UserRepository userRepository, ConversationRepository conversationRepository,
                        MessageRepository messageRepository) {
                this.userRepository = userRepository;
                this.conversationRepository = conversationRepository;
                this.messageRepository = messageRepository;
        }

        @Override
        public MessageDto addMessageToConversation(String message, Long conversationId, Long sender)
                        throws GraphqlEntityNotFound {
                if (message == null || conversationId == null || sender == null) {
                        throw new GraphqlEntityNotFound(
                                        "Invalid params provided. Message/conversation/sender not found");
                }

                User author = this.userRepository.findById(sender)
                                .orElseThrow(() -> new GraphqlEntityNotFound(
                                                "Sender with id " + sender + " not found"));

                Conversation conversation = this.conversationRepository.findById(conversationId)
                                .orElseThrow(() -> new GraphqlEntityNotFound(
                                                "Conversation with id " + conversationId + " not found"));

                Message newMessage = Message.builder().conversation(conversation).sender(author)
                                .messageStatus(MessageStatus.SENT).build();

                Message savedMessage = this.messageRepository.save(newMessage);

                MessageDto messageDto = buildMessageDto(savedMessage);

                return messageDto;
        }

        private MessageDto buildMessageDto(Message message) {
                UserSummaryDto userSummary = UserSummaryDto.builder().id(message.getSender().getId())
                                .firstname(message.getSender().getFirstname())
                                .lastname(message.getSender().getLastname())
                                .username(message.getSender().getUsername()).email(message.getSender().getEmail())
                                .phonenumber(message.getSender().getPhonenumber()).bio(message.getSender().getBio())
                                .image(message.getSender().getImage()).build();

                ConversationInfo conversationInfo = ConversationInfo.builder()
                                .lastMessage(message.getConversation().getLastMessage())
                                .createdAt(message.getConversation().getCreatedAt())
                                .updatedAt(message.getConversation().getUpdatedAt())
                                .build();
                return MessageDto.builder().id(message.getId()).conversation(conversationInfo)
                                .messageStatus(message.getMessageStatus()).createdAt(message.getCreatedAt())
                                .updatedAt(message.getUpdatedAt()).sender(userSummary).build();
        }

}
