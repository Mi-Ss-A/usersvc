package com.wibeechat.missa.controller.history;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.dto.history.ChatSaveResponse;
import com.wibeechat.missa.entity.postgresql.ChatMessage;
import com.wibeechat.missa.service.history.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

        private final ChatService chatService;

        @GetMapping("/dates")
        @LoginRequired
        @Operation(summary = "채팅 메시지 목록 전체 보기", description = "일자별 채팅 메시지 목록을 가져옵니다")
        @ApiResponse(responseCode = "200", description = "일자별 채팅 메시지 불러오기 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatSaveResponse.class)))
        public ResponseEntity<List<LocalDate>> getChatHistoryDates(@CurrentUser String userNo) {
                List<LocalDate> messages = chatService.getDistinctDates(userNo);
                return ResponseEntity.ok(messages);
        }

        @GetMapping
        @LoginRequired
        @Operation(summary = "채팅 메시지 보기", description = "채팅 메시지를 가져옵니다")
        @ApiResponse(responseCode = "200", description = "일자에 따른 메시지 불러오기 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatSaveResponse.class)))
        public ResponseEntity<List<ChatMessage>> getChatHistory(@CurrentUser String userNo, String date) {
                // String을 LocalDate로 변환
                List<ChatMessage> messages = chatService.getChatMessagesByDate(userNo, date);
                return ResponseEntity.ok(messages);
        }
}