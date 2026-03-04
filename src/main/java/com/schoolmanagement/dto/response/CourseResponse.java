package com.schoolmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String name;
    private String courseCode;
    private String description;
    private Integer credits;
    private Integer maxCapacity;
    private Long currentEnrollments;
    private boolean active;
    private Long teacherId;
    private String teacherName;
    private LocalDateTime createdAt;
}
