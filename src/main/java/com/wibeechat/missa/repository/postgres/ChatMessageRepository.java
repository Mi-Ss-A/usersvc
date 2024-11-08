package com.wibeechat.missa.repository.postgres;

import com.wibeechat.missa.entity.postgresql.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository  extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByUserNo(String userNo);
}
