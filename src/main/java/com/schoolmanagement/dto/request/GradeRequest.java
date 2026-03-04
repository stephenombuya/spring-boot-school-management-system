package com.schoolmanagement.dto.request;

import com.schoolmanagement.model.Grade;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class GradeRequest {
    @NotNull private Long studentId;
    @NotNull private Long courseId;
    @NotBlank @Size(max = 20) private String semester;
    @NotNull private Grade.AssessmentType assessmentType;
    @NotNull @DecimalMin("0.00") @DecimalMax("100.00") private BigDecimal score;
    @DecimalMin("1.00") @DecimalMax("100.00") private BigDecimal maxScore;
    @DecimalMin("0.00") @DecimalMax("100.00") private BigDecimal weightPercentage;
    @Size(max = 500) private String comments;
}
