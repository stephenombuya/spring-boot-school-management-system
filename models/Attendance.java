package com.schoolmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendances")
public class Attendance {
    
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
    private LocalDate date;
    
    private LocalTime time;
    
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    
    private String remarks;
    
    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED
    }
}
