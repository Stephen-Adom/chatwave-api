package com.chats.chatwave.model.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthUserDto {
    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String email;

    private String phonenumber;

    private Boolean enabled;

    private String bio;

    private String image;

}
