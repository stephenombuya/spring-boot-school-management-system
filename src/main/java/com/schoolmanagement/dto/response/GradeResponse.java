package com.schoolmanagement.dto.response;

import com.schoolmanagement.model.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GradeResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private String semester;
    private Grade.AssessmentType assessmentType;
    private BigDecimal score;
    private BigDecimal maxScore;
    private BigDecimal weightPercentage;
    private String letterGrade;
    private BigDecimal gradePoints;
    private String comments;
    private String gradedByName;
    private LocalDateTime createdAt;
}
