package com.wibeechat.missa.entity.postgresql;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageStatus {
    private boolean isProcessed;
    private String errorMessage;
}
