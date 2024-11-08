package com.wibeechat.missa.userhistory;

import com.wibeechat.missa.entity.postgresql.*;
import com.wibeechat.missa.repository.postgres.ChatMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


import com.wibeechat.missa.entity.postgresql.ChatMessage;
import com.wibeechat.missa.entity.postgresql.ChatMessage.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserHistoryControllerTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;  // Repository 주입

    @Test
    @DisplayName("채팅 메시지 생성 및 저장 테스트")
    public void createAndSaveChatMessageTest() {
        // Given
        String userNo = "ec30ee0d-c663-42aa-bf81-448e2d4f50c2";
        String userQuestion = "사용자 질문";
        String aiResponse = "AI 응답";

        // When - 사용자 메시지 생성 및 저장
        ChatMessage userMessage = ChatMessage.builder()
                .userNo(userNo)
                .message(Message.builder()
                        .sender(SenderType.USER)
                        .content(userQuestion)
                        .timestamp(LocalDateTime.now())
                        .build())
                .metadata(MessageMetadata.builder()
                        .messageLength(userQuestion.length())
                        .build())
                .status(MessageStatus.builder()
                        .isProcessed(false)
                        .build())
                .build();

        ChatMessage savedUserMessage = chatMessageRepository.save(userMessage);

        // Then - 사용자 메시지 검증
        assertThat(savedUserMessage).isNotNull();
        assertThat(savedUserMessage.getId()).isNotNull();  // ID가 생성되었는지 확인
        assertThat(savedUserMessage.getUserNo()).isEqualTo(userNo);
        assertThat(savedUserMessage.getMessage().getSender()).isEqualTo(SenderType.USER);
        assertThat(savedUserMessage.getMessage().getContent()).isEqualTo(userQuestion);
        assertThat(savedUserMessage.getMessage().getTimestamp()).isNotNull();

        // When - AI 메시지 생성 및 저장
        ChatMessage aiMessage = ChatMessage.builder()
                .userNo(userNo)
                .message(Message.builder()
                        .sender(SenderType.AI)
                        .content(aiResponse)
                        .timestamp(LocalDateTime.now())
                        .build())
                .metadata(MessageMetadata.builder()
                        .messageLength(aiResponse.length())
                        .build())
                .status(MessageStatus.builder()
                        .isProcessed(true)
                        .build())
                .build();

        ChatMessage savedAiMessage = chatMessageRepository.save(aiMessage);

        // Then - AI 메시지 검증
        assertThat(savedAiMessage).isNotNull();
        assertThat(savedAiMessage.getId()).isNotNull();
        assertThat(savedAiMessage.getUserNo()).isEqualTo(userNo);
        assertThat(savedAiMessage.getMessage().getSender()).isEqualTo(SenderType.AI);
        assertThat(savedAiMessage.getMessage().getContent()).isEqualTo(aiResponse);
        assertThat(savedAiMessage.getMessage().getTimestamp()).isNotNull();

        // DB에서 조회하여 실제 저장 여부 확인
        List<ChatMessage> savedMessages = chatMessageRepository.findByUserNo(userNo);
        assertThat(savedMessages).hasSize(2);
    }
}