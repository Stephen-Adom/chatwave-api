package com.chats.chatwave.model.Dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ConversationDto {
    private Long id;
    private String lastMessage;
    private Date createdAt;
    private Date updatedAt;
    private List<UserSummaryDto> users = new ArrayList<>();
}
