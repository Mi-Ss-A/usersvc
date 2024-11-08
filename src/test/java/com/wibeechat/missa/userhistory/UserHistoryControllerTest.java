package com.wibeechat.missa.userhistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibeechat.missa.controller.history.HistoryController;
import com.wibeechat.missa.dto.history.ChatSaveRequest;
import com.wibeechat.missa.dto.history.ChatSaveResponse;
import com.wibeechat.missa.service.history.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistoryController.class)
class UserHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @Test
    @DisplayName("메시지 저장 성공 테스트")
    void saveMessageSuccess() throws Exception {
        // Given
        String userNo = "123";
        String content = "테스트 메시지";
        String sender = "USER";

        ChatSaveRequest request = ChatSaveRequest.builder()
                .content(content)
                .sender(sender)
                .build();

        ChatSaveResponse response = ChatSaveResponse.builder()
                .id(1L)
                .content(content)
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .isProcessed(false)
                .build();

        given(chatService.saveMessage(eq(userNo), any(ChatSaveRequest.class)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(post("/api/history")
                        .header("Session-ID", userNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.sender").value(sender));

        verify(chatService).saveMessage(eq(userNo), any(ChatSaveRequest.class));
    }

    @Test
    @DisplayName("Session-ID 헤더 누락 시 실패 테스트")
    void saveMessageWithoutSessionId() throws Exception {
        // Given
        ChatSaveRequest request = ChatSaveRequest.builder()
                .content("테스트 메시지")
                .sender("USER")
                .build();

        // When & Then
        mockMvc.perform(post("/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 형식의 요청 데이터 테스트")
    void saveMessageWithInvalidRequest() throws Exception {
        // Given
        String userNo = "123";
        ChatSaveRequest request = ChatSaveRequest.builder()
                .content("")  // 빈 내용
                .sender("INVALID_SENDER")  // 잘못된 발신자 타입
                .build();

        // When & Then
        mockMvc.perform(post("/api/history")
                        .header("Session-ID", userNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}