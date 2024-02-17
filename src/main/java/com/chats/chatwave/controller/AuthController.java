package com.chats.chatwave.controller;

import java.util.HashMap;
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
import com.chats.chatwave.model.User;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public String authenticateUser() {
        return "User Login";
    }

    @PostMapping("register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRequestModel user,
            BindingResult bindingResult) throws ValidationErrorsException,
            EntityNotFoundException {
        User newUser = this.authenticationService.userRegistration(user,
                bindingResult);

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("data", newUser);
        responseBody.put("status", HttpStatus.CREATED);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
