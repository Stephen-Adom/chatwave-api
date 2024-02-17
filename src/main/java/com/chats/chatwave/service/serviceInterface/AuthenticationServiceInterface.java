package com.chats.chatwave.service.serviceInterface;

import org.springframework.validation.BindingResult;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.RequestModel.UserRequestModel;

public interface AuthenticationServiceInterface {
    public User userRegistration(UserRequestModel requestBody, BindingResult bindingResult)
            throws ValidationErrorsException, EntityNotFoundException;
}
