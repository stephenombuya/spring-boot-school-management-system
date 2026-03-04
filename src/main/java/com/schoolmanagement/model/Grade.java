package com.schoolmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id", "semester", "assessment_type"})
})
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", nullable = false)
    private AssessmentType assessmentType;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal maxScore = BigDecimal.valueOf(100);

    @Column(name = "weight_percentage", precision = 5, scale = 2)
    private BigDecimal weightPercentage;

    @Column(name = "letter_grade", length = 3)
    private String letterGrade;

    @Column(name = "grade_points", precision = 3, scale = 2)
    private BigDecimal gradePoints;

    @Column(name = "comments", length = 500)
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private User gradedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum AssessmentType {
        MIDTERM, FINAL, QUIZ, ASSIGNMENT, LAB, PROJECT, PARTICIPATION
    }

    @PrePersist
    @PreUpdate
    public void calculateLetterGrade() {
        if (score != null && maxScore != null && maxScore.compareTo(BigDecimal.ZERO) > 0) {
            double percentage = score.divide(maxScore, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
            if (percentage >= 90) { letterGrade = "A"; gradePoints = BigDecimal.valueOf(4.0); }
            else if (percentage >= 80) { letterGrade = "B"; gradePoints = BigDecimal.valueOf(3.0); }
            else if (percentage >= 70) { letterGrade = "C"; gradePoints = BigDecimal.valueOf(2.0); }
            else if (percentage >= 60) { letterGrade = "D"; gradePoints = BigDecimal.valueOf(1.0); }
            else { letterGrade = "F"; gradePoints = BigDecimal.ZERO; }
        }
    }
}
