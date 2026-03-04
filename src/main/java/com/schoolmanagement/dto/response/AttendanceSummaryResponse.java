package com.schoolmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttendanceSummaryResponse {
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private long totalClasses;
    private long presentCount;
    private long absentCount;
    private long lateCount;
    private long excusedCount;
    private double attendancePercentage;
}
