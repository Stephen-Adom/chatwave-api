package com.chats.chatwave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chats.chatwave.model.UserConversation;

@Repository
public interface UserConversationRepository extends JpaRepository<UserConversation, Long> {

    public List<UserConversation> findAllByUserId(Long userId);
}
