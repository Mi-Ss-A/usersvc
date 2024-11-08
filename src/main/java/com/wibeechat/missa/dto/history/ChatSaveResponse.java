package com.wibeechat.missa.dto.history;


import com.wibeechat.missa.entity.postgresql.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSaveResponse {
    @Schema(description = "메시지 ID")
    private Long id;

    @Schema(description = "메시지 내용")
    private String content;

    @Schema(description = "발신자 유형", allowableValues = {"USER", "AI"})
    private String sender;

    @Schema(description = "메시지 전송 시간")
    private LocalDateTime timestamp;

    @Schema(description = "처리 완료 여부")
    private boolean isProcessed;

    @Schema(description = "유저의 세션 아이디 및 pk")
    private String userno;

    public static ChatSaveResponse from(ChatMessage message) {
        return ChatSaveResponse.builder()
                .id(message.getId())
                .userno(message.getUserNo())
                .content(message.getMessage().getContent())
                .sender(message.getMessage().getSender().name())
                .timestamp(message.getMessage().getTimestamp())
                .isProcessed(message.getStatus().isProcessed())
                .build();
    }
}