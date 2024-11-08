package com.wibeechat.missa.dto.history;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wibeechat.missa.entity.postgresql.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 이력 날짜 목록 응답")
public class ChatHistoryResponse {

    @Schema(description = "채팅 이력 날짜 목록")
    private List<HistoryItem> userHistoryList;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "채팅 이력 날짜 항목")
    public static class HistoryItem {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Schema(description = "채팅 발생 일시", example = "2024-11-08 12:23:58")
        private LocalDateTime historyDate;

        // Entity를 DTO로 변환하는 정적 메서드
        public static HistoryItem from(ChatMessage message) {
            return HistoryItem.builder()
                    .historyDate(message.getMessage().getTimestamp())
                    .build();
        }
    }

    // Entity 리스트를 DTO로 변환하는 정적 메서드
    public static ChatHistoryResponse from(List<ChatMessage> messages) {
        List<HistoryItem> historyItems = messages.stream()
                .map(HistoryItem::from)
                .toList();

        return ChatHistoryResponse.builder()
                .userHistoryList(historyItems)
                .build();
    }
}
