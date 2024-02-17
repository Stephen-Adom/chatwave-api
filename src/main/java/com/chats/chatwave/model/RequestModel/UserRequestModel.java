package com.chats.chatwave.model.RequestModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class UserRequestModel {

    @NotNull(message = "")
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private String password;
}
