package com.chats.chatwave.model.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserSummaryDto {
    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String phonenumber;

    private String bio;

    private String image;
}
