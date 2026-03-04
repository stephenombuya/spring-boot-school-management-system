package com.schoolmanagement.services;

import com.schoolmanagement.dto.CourseDTO;
import com.schoolmanagement.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    Optional<CourseDTO> getCourseById(Long id);
    Optional<CourseDTO> getCourseByCourseCode(String courseCode);
    List<CourseDTO> getCoursesByDepartment(String department);
    List<CourseDTO> getCoursesByLevel(Course.CourseLevel level);
    CourseDTO createCourse(Course course);
    CourseDTO updateCourse(Long id, Course course);
    void deleteCourse(Long id);
    boolean existsByCourseCode(String courseCode);
}
