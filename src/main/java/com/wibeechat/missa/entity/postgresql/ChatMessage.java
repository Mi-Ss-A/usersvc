package com.wibeechat.missa.entity.postgresql;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(schema = "mydb2", name = "chat_messages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_no")
    private String userNo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sender", column = @Column(name = "message_sender")),
            @AttributeOverride(name = "content", column = @Column(name = "message_content")),
            @AttributeOverride(name = "timestamp", column = @Column(name = "message_timestamp"))
    })
    private Message message;

    @Embeddable
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        @Enumerated(EnumType.STRING)
        private SenderType sender;
        private String content;
        private LocalDateTime timestamp;
    }
    
    public enum SenderType {
        USER, AI
    }
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "messageLength", column = @Column(name = "message_length")),
            @AttributeOverride(name = "processingTime", column = @Column(name = "processing_time"))
    })
    private MessageMetadata metadata;

    @Data
    @Embeddable
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageMetadata {
        private Integer messageLength;
        private Long processingTime;
    }

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "isProcessed", column = @Column(name = "is_processed")),
            @AttributeOverride(name = "errorMessage", column = @Column(name = "error_message"))
    })
    private MessageStatus status;
    
    @Data
    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageStatus {
        private boolean isProcessed;
        private String errorMessage;
    }

    @PrePersist
    protected void onCreate() {
        if (message != null) {
            message.timestamp = LocalDateTime.now();

            if (message.getContent() != null) {
                metadata = MessageMetadata.builder()
                        .messageLength(message.getContent().length())
                        .build();
            }

            status = MessageStatus.builder()
                    .isProcessed(message.getSender() == SenderType.AI)
                    .build();
        }
    }
}