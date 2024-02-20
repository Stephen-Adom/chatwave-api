package com.chats.chatwave.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chats.chatwave.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
