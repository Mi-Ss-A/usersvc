package com.wibeechat.missa.entity.postgresql;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    @Enumerated(EnumType.STRING)
    private SenderType sender;
    private String content;
    private LocalDateTime timestamp;
}