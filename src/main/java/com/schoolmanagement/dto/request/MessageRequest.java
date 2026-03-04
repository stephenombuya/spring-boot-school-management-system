package com.schoolmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull private Long receiverId;
    @NotBlank @Size(max = 255) private String subject;
    @NotBlank private String content;
    private Long relatedCourseId;
    private Long parentMessageId;
}
