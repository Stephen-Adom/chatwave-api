package com.chats.chatwave.model.ResponseModel;

import org.springframework.http.HttpStatus;

import com.chats.chatwave.model.Dto.AuthUserDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthenticateResponse {
    private AuthUserDto data;
    private String accessToken;
    private String refreshToken;
    private HttpStatus status;
}
