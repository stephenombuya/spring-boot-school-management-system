package com.schoolmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grades")
public class Grade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(nullable = false)
    private String assignmentName;
    
    private String assignmentType;  // Quiz, Test, Project, Homework, etc.
    
    @Column(nullable = false)
    private Double score;
    
    private Double maxScore;
    
    @Column(nullable = false)
    private Double weightage;  // Percentage of total grade
    
    private LocalDate dateRecorded;
    
    private String feedback;
}
