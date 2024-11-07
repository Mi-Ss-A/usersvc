package com.wibeechat.missa.entity.postgresql;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(schema = "mydb2",name = "chat_messages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "user_question")
    private String userQuestion;

    @Column(name = "ai_answer")
    private String aiAnswer;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "questionLength", column = @Column(name = "question_length")),
            @AttributeOverride(name = "answerLength", column = @Column(name = "answer_length")),
            @AttributeOverride(name = "processingTime", column = @Column(name = "processing_time"))
    })
    private MessageMetadata metadata;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "isProcessed", column = @Column(name = "is_processed")),
            @AttributeOverride(name = "errorMessage", column = @Column(name = "error_message"))
    })
    private MessageStatus status;

    @Data
    @Embeddable
    public static class MessageMetadata {
        private Integer questionLength;
        private Integer answerLength;
        private Long processingTime;
    }

    @Data
    @Embeddable
    public static class MessageStatus {
        private boolean isProcessed;
        private String errorMessage;
    }
}