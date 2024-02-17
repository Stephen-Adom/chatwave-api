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
import com.chats.chatwave.model.RequestModel.UserLoginModel;
import com.chats.chatwave.model.RequestModel.UserRequestModel;
import com.chats.chatwave.service.AuthenticationService;
import com.chats.chatwave.service.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final JwtService jwtService;

    public AuthController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody UserLoginModel user,
            BindingResult bindingResult) throws ValidationErrorsException, EntityNotFoundException {
        User newUser = this.authenticationService.authenticateUser(user, bindingResult);

        String token = jwtService.generateToken(newUser);
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("data", newUser);
        responseBody.put("token", token);
        responseBody.put("status", HttpStatus.OK);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody UserRequestModel user,
            BindingResult bindingResult) throws ValidationErrorsException,
            EntityNotFoundException {
        User newUser = this.authenticationService.userRegistration(user,
                bindingResult);

        String token = jwtService.generateToken(newUser);
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("data", newUser);
        responseBody.put("token", token);
        responseBody.put("status", HttpStatus.CREATED);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

}
