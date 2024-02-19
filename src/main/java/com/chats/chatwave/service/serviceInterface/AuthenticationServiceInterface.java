package com.chats.chatwave.service.serviceInterface;

import java.io.IOException;

import org.springframework.validation.BindingResult;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.RequestModel.AuthenticateRequest;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.model.ResponseModel.AuthenticateResponse;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationServiceInterface {
        public AuthenticateResponse userRegistration(UserRequestModel requestBody, BindingResult bindingResult)
                        throws ValidationErrorsException, EntityNotFoundException;

        public AuthenticateResponse authenticateUser(AuthenticateRequest requestBody, BindingResult bindingResult)
                        throws ValidationErrorsException, EntityNotFoundException;

        public void refreshToken(HttpServletRequest request, HttpServletResponse response)
                        throws EntityNotFoundException, StreamWriteException, DatabindException, IOException;
}
