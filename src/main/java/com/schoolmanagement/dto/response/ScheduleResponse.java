package com.schoolmanagement.dto.response;

import com.schoolmanagement.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleResponse {
    private Long id;
    private Long courseId;
    private String courseName;
    private String courseCode;
    private String title;
    private String description;
    private Schedule.ScheduleType eventType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private boolean recurring;
    private String recurrencePattern;
}
