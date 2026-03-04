package com.schoolmanagement.dto.request;

import com.schoolmanagement.model.Schedule;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    @NotNull private Long courseId;
    @NotBlank @Size(max = 200) private String title;
    @Size(max = 500) private String description;
    @NotNull private Schedule.ScheduleType eventType;
    @NotNull private LocalDateTime startTime;
    @NotNull private LocalDateTime endTime;
    @Size(max = 100) private String location;
    private boolean recurring;
    private String recurrencePattern;
}
