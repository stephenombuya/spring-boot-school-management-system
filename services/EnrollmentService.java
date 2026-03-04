package com.schoolmanagement.services;

import com.schoolmanagement.dto.EnrollmentDTO;
import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Enrollment;
import com.schoolmanagement.model.Student;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    List<EnrollmentDTO> getAllEnrollments();
    Optional<EnrollmentDTO> getEnrollmentById(Long id);
    List<EnrollmentDTO> getEnrollmentsByStudent(Student student);
    List<EnrollmentDTO> getEnrollmentsByCourse(Course course);
    List<EnrollmentDTO> getEnrollmentsByStudentAndSemester(Student student, String semester, Integer academicYear);
    List<EnrollmentDTO> getEnrollmentsByCourseAndSemester(Course course, String semester, Integer academicYear);
    EnrollmentDTO createEnrollment(Enrollment enrollment);
    EnrollmentDTO updateEnrollment(Long id, Enrollment enrollment);
    void deleteEnrollment(Long id);
    boolean isStudentEnrolled(Student student, Course course, String semester, Integer academicYear);
}
