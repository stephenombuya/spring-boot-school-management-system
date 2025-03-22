package com.schoolmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student extends User {
    
    @Column(unique = true)
    private String studentId;
    
    private LocalDate dateOfBirth;
    
    private String guardianName;
    
    private String guardianContact;
    
    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments = new HashSet<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Attendance> attendances = new HashSet<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Grade> grades = new HashSet<>();
    
    public enum GradeLevel {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR
    }
}
