package com.wibeechat.missa.repository.postgres;

import com.wibeechat.missa.entity.postgresql.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository  extends JpaRepository<ChatMessage, String> {
}
