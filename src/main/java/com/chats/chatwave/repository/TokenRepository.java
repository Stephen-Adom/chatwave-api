package com.chats.chatwave.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chats.chatwave.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findAllByUserId(Long userId);

    Optional<Token> findByToken(String token);
}
