package com.chats.chatwave.model.ResponseModel;

import org.springframework.http.HttpStatus;

import com.chats.chatwave.model.Dto.AuthUserDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthenticateResponse {

    private AuthUserDto data;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private HttpStatus status;
}
