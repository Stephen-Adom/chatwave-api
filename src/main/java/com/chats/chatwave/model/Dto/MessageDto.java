package com.chats.chatwave.model.Dto;

import java.util.Date;

import com.chats.chatwave.model.MessageStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class MessageDto {

    private Long id;

    private ConversationInfo conversation;

    private UserSummaryDto sender;

    private MessageStatus messageStatus;

    private Date createdAt;

    private Date updatedAt;
}
