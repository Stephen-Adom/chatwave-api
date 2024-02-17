package com.chats.chatwave.service.serviceInterface;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserServiceInterface {
    public UserDetails loadUserByUsername(String username);
}
