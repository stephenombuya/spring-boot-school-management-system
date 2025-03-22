package com.schoolmanagement.dtos;

import com.schoolmanagement.model.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private String semester;
    private Integer academicYear;
    private LocalDate enrollmentDate;
    private Enrollment.EnrollmentStatus status;
}
