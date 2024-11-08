package com.wibeechat.missa.controller.history;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.dto.history.ChatHistoryResponse;
import com.wibeechat.missa.dto.history.ChatSaveRequest;
import com.wibeechat.missa.dto.history.ChatSaveResponse;
import com.wibeechat.missa.entity.postgresql.ChatMessage;
import com.wibeechat.missa.service.history.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

    private final ChatService chatService;


    @PostMapping
    @LoginRequired
    @Operation(
            summary = "채팅 메시지 저장",
            description = "새로운 채팅 메시지를 저장합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "메시지 저장 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChatSaveResponse.class)
            )
    )
    public ResponseEntity<ChatSaveResponse> saveMessage(
            @CurrentUser String userId,  // 세션에서 자동으로 userId를 가져옴
            @RequestBody ChatSaveRequest request
    ) {
        return ResponseEntity.ok(chatService.saveMessage(userId, request));
    }

    @GetMapping("/dates")
    @LoginRequired
    @Operation(
            summary = "채팅 메시지 목록 전체 보기",
            description = "일자별 채팅 메시지 목록을 가져옵니다"
    )
    @ApiResponse(
            responseCode = "200",
            description = "일자별 채팅 메시지 불러오기 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChatSaveResponse.class)
            )
    )
    public ResponseEntity<List<LocalDate>> getChatHistoryDates(@CurrentUser String userNo) {
        List<LocalDate> messages = chatService.getDistinctDates(userNo);
        return ResponseEntity.ok(messages);
    }
}