package com.wibeechat.missa.repository.postgres;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wibeechat.missa.entity.postgresql.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserNo(String userNo);

    List<ChatMessage> findByUserNoOrderByMessage_TimestampDesc(String userNo);

    List<ChatMessage> findByUserNoAndMessage_TimestampBetweenOrderByMessage_TimestampAsc(
            String userNo,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime);

}
