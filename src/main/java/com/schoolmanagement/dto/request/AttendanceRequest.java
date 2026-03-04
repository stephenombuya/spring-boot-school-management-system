package com.schoolmanagement.dto.request;

import com.schoolmanagement.model.Attendance;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AttendanceRequest {
    @NotNull private Long studentId;
    @NotNull private Long courseId;
    @NotNull private LocalDate attendanceDate;
    @NotNull private Attendance.AttendanceStatus status;
    @Size(max = 255) private String remarks;
}
