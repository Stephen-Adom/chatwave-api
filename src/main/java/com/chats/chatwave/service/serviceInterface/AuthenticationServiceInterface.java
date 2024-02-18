package com.chats.chatwave.service.serviceInterface;

import org.springframework.validation.BindingResult;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.RequestModel.AuthenticateRequest;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.model.ResponseModel.AuthenticateResponse;

public interface AuthenticationServiceInterface {
        public AuthenticateResponse userRegistration(UserRequestModel requestBody, BindingResult bindingResult)
                        throws ValidationErrorsException, EntityNotFoundException;

        public AuthenticateResponse authenticateUser(AuthenticateRequest requestBody, BindingResult bindingResult)
                        throws ValidationErrorsException, EntityNotFoundException;
}
