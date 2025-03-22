package com.schoolmanagement.dtos;

import com.schoolmanagement.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String name;
    private String description;
    private Integer credits;
    private String department;
    private Course.CourseLevel level;
    private boolean active;
}
