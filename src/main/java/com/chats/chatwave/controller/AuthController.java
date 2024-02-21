package com.chats.chatwave.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chats.chatwave.Exception.EntityNotFoundException;
import com.chats.chatwave.Exception.ValidationErrorsException;
import com.chats.chatwave.model.RequestModel.AuthenticateRequest;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.model.ResponseModel.AuthenticateResponse;
import com.chats.chatwave.service.AuthenticationService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticateResponse> authenticateUser(@Valid @RequestBody AuthenticateRequest request,
            BindingResult bindingResult) throws ValidationErrorsException, EntityNotFoundException {
        AuthenticateResponse response = this.authenticationService.authenticateUser(request, bindingResult);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<AuthenticateResponse> registerUser(@Valid @RequestBody UserRequestModel user,
            BindingResult bindingResult) throws ValidationErrorsException,
            EntityNotFoundException {
        AuthenticateResponse response = this.authenticationService.userRegistration(user,
                bindingResult);

        return new ResponseEntity<AuthenticateResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws StreamWriteException, DatabindException, EntityNotFoundException, IOException {
        this.authenticationService.refreshToken(request, response);
    }

    @PostMapping("logout")
    public ResponseEntity<Map<String, String>> signOut(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = this.authenticationService.unauthenticateUser(request, response);

        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }
}
