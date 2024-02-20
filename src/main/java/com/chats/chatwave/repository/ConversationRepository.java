package com.chats.chatwave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chats.chatwave.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

}
