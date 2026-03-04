package com.schoolmanagement.services;

import com.schoolmanagement.dto.StudentDTO;
import com.schoolmanagement.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<StudentDTO> getAllStudents();
    Optional<StudentDTO> getStudentById(Long id);
    Optional<StudentDTO> getStudentByStudentId(String studentId);
    Optional<StudentDTO> getStudentByEmail(String email);
    StudentDTO createStudent(Student student);
    StudentDTO updateStudent(Long id, Student student);
    void deleteStudent(Long id);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
}
