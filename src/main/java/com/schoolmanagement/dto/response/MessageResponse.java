package com.schoolmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String subject;
    private String content;
    private boolean read;
    private LocalDateTime readAt;
    private Long relatedCourseId;
    private Long parentMessageId;
    private LocalDateTime sentAt;
}
