package com.chats.chatwave.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    @GetMapping("chats")
    public ResponseEntity<Map<String, String>> getChats() {
        return ResponseEntity.status(0).body(null);
    }
}
