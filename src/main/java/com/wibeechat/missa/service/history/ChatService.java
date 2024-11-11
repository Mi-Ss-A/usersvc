package com.wibeechat.missa.service.history;


import com.wibeechat.missa.dto.history.ChatSaveRequest;
import com.wibeechat.missa.dto.history.ChatSaveResponse;
import com.wibeechat.missa.entity.postgresql.ChatMessage;
import com.wibeechat.missa.repository.postgres.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;


    @Transactional
    public List<LocalDate> getDistinctDates(String userNo) {
        return chatMessageRepository.findByUserNoOrderByMessage_TimestampDesc(userNo)
                .stream()
                .map(chatMessage -> chatMessage.getMessage().getTimestamp().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChatMessage> getChatMessagesByDate(String userNo, String date) {
        LocalDate targetDate = LocalDate.parse(date);
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);

        return chatMessageRepository.findByUserNoAndMessage_TimestampBetweenOrderByMessage_TimestampAsc(
                userNo,
                startOfDay,
                endOfDay
        );
    }
}