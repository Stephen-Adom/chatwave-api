package com.chats.chatwave.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("login")
    public String authenticateUser() {
        return "User Login";
    }

    @PostMapping("register")
    public String registerUser() {
        return "";
    }
}
