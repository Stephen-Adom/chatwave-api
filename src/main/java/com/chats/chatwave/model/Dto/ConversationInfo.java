package com.chats.chatwave.model.Dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ConversationInfo {
    private Long id;
    private String lastMessage;
    private Date createdAt;
    private Date updatedAt;
}
