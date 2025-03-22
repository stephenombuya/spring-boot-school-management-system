package com.schoolmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String courseCode;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    private Integer credits;
    
    @Column(nullable = false)
    private String department;
    
    @Enumerated(EnumType.STRING)
    private CourseLevel level;
    
    private boolean active = true;
    
    @ManyToMany(mappedBy = "coursesTeaching")
    private Set<Teacher> teachers = new HashSet<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments = new HashSet<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<Schedule> schedules = new HashSet<>();
    
    public enum CourseLevel {
        FRESHMAN, SOPHOMORE, JUNIOR, SENIOR, GRADUATE
    }
}
