package com.chats.chatwave.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.chats.chatwave.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserDetails> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhonenumber(String phonenumber);
}
