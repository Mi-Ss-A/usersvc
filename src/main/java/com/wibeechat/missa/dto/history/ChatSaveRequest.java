package com.wibeechat.missa.dto.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 저장 요청")
public class ChatSaveRequest {

    @Schema(
            description = "메시지 내용",
            example = "메시지 내용",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @Schema(
            description = "발신자 유형",
            example = "USER",
            allowableValues = {"USER", "AI"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String sender;
}