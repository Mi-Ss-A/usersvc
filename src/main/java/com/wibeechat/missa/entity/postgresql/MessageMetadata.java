package com.wibeechat.missa.entity.postgresql;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageMetadata {
    private Integer messageLength;
    private Long processingTime;
}
