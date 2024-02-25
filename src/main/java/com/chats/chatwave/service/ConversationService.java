package com.chats.chatwave.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chats.chatwave.Exception.GraphqlEntityNotFound;
import com.chats.chatwave.Exception.GraphqlValidationErrorException;
import com.chats.chatwave.model.Conversation;
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.UserConversation;
import com.chats.chatwave.model.Dto.ConversationDto;
import com.chats.chatwave.model.Dto.UserSummaryDto;
import com.chats.chatwave.model.RequestModel.ConversationRequest;
import com.chats.chatwave.repository.ConversationRepository;
import com.chats.chatwave.repository.UserConversationRepository;
import com.chats.chatwave.repository.UserRepository;
import com.chats.chatwave.service.serviceInterface.ConversationServiceInterface;

@Service
@SuppressWarnings("null")
public class ConversationService implements ConversationServiceInterface {

        private final UserRepository userRepository;

        private final ConversationRepository conversationRepository;

        private final UserConversationRepository userConversationRepository;

        public ConversationService(UserRepository userRepository, ConversationRepository conversationRepository,
                        UserConversationRepository userConversationRepository) {
                this.userRepository = userRepository;
                this.conversationRepository = conversationRepository;
                this.userConversationRepository = userConversationRepository;
        }

        @Override
        public ConversationDto createConversation(ConversationRequest request) throws GraphqlValidationErrorException {
                if (request.getUser1() == null || request.getUser2() == null) {
                        throw new GraphqlValidationErrorException("User IDs cannot be null");
                }

                User user1 = this.userRepository.findById(request.getUser1())
                                .orElseThrow(() -> new GraphqlEntityNotFound(
                                                "User id " + request.getUser1() + " not found"));

                User user2 = this.userRepository.findById(request.getUser2())
                                .orElseThrow(() -> new GraphqlEntityNotFound(
                                                "User id " + request.getUser2() + " not found"));

                Conversation newConversation = this.conversationRepository.save(new Conversation());

                List<UserConversation> userConverstions = List.of(
                                UserConversation.builder().user(user1).conversation(newConversation)
                                                .build(),
                                UserConversation.builder().user(user2).conversation(newConversation)
                                                .build());

                List<UserConversation> savedUserConversations = this.userConversationRepository
                                .saveAll(userConverstions);

                return buildConversationDto(newConversation, savedUserConversations);
        }

        @Override
        public List<ConversationDto> allUserConversations(Long userid) throws GraphqlEntityNotFound {
                if (userid == null) {
                        throw new GraphqlEntityNotFound("User id param not found");
                }

                this.userRepository.findById(userid)
                                .orElseThrow(() -> new GraphqlEntityNotFound("User with id " + userid + " not found"));

                List<UserConversation> allUserConversations = this.userConversationRepository.findAllByUserId(userid);

                List<ConversationDto> allConversationDto = allUserConversations.stream().map(
                                userConversation -> this.buildConversationModel(userConversation.getConversation()))
                                .collect(Collectors.toList());

                return allConversationDto;
        }

        private ConversationDto buildConversationDto(Conversation conversation,
                        List<UserConversation> savedUserConversations) {

                List<UserSummaryDto> authUsers = savedUserConversations.stream()
                                .map(user -> buildUserDto(user.getUser()))
                                .collect(Collectors.toList());

                return ConversationDto.builder().id(conversation.getId()).lastMessage(conversation.getLastMessage())
                                .createdAt(conversation.getCreatedAt()).updatedAt(conversation.getUpdatedAt())
                                .users(authUsers).build();
        }

        private ConversationDto buildConversationModel(Conversation conversation) {
                List<UserSummaryDto> authUsers = conversation.getUsers().stream()
                                .map(user -> buildUserDto(user.getUser()))
                                .collect(Collectors.toList());

                return ConversationDto.builder().id(conversation.getId()).lastMessage(conversation.getLastMessage())
                                .createdAt(conversation.getCreatedAt()).updatedAt(conversation.getUpdatedAt())
                                .users(authUsers).build();
        }

        private UserSummaryDto buildUserDto(User user) {
                return UserSummaryDto.builder().id(user.getId()).firstname(user.getFirstname())
                                .lastname(user.getLastname())
                                .email(user.getEmail()).phonenumber(user.getPhonenumber()).username(user.getUsername())
                                .build();
        }
}
