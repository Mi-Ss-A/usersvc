package com.wibeechat.missa.repository.postgres;

import com.wibeechat.missa.entity.postgresql.ChatMessage;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository  extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserNo(String userNo);
    List<ChatMessage> findByUserNoOrderByMessage_TimestampDesc(String userNo);
    List<ChatMessage> findByUserNoAndMessage_TimestampBetweenOrderByMessage_TimestampAsc(
            String userNo,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

}
