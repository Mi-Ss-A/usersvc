package com.wibeechat.missa.service.history;


import com.wibeechat.missa.dto.history.ChatSaveRequest;
import com.wibeechat.missa.dto.history.ChatSaveResponse;
import com.wibeechat.missa.entity.postgresql.ChatMessage;
import com.wibeechat.missa.repository.postgres.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatSaveResponse saveMessage(String userNo, ChatSaveRequest request) {
        ChatMessage message = ChatMessage.builder()
                .userNo(userNo)
                .message(ChatMessage.Message.builder()
                        .sender(ChatMessage.SenderType.valueOf(request.getSender()))
                        .content(request.getContent())
                        .timestamp(LocalDateTime.now())
                        .build())
                .metadata(ChatMessage.MessageMetadata.builder()
                        .messageLength(request.getContent().length())
                        .build())
                .status(ChatMessage.MessageStatus.builder()
                        .isProcessed(ChatMessage.SenderType.valueOf(request.getSender()) == ChatMessage.SenderType.AI)
                        .build())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);

        return ChatSaveResponse.from(savedMessage);
    }
}