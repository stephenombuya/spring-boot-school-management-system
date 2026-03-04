package com.schoolmanagement.services;

import com.schoolmanagement.dto.TeacherDTO;
import com.schoolmanagement.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<TeacherDTO> getAllTeachers();
    Optional<TeacherDTO> getTeacherById(Long id);
    Optional<TeacherDTO> getTeacherByEmployeeId(String employeeId);
    Optional<TeacherDTO> getTeacherByEmail(String email);
    List<TeacherDTO> getTeachersByDepartment(String department);
    TeacherDTO createTeacher(Teacher teacher);
    TeacherDTO updateTeacher(Long id, Teacher teacher);
    void deleteTeacher(Long id);
    boolean existsByEmployeeId(String employeeId);
    boolean existsByEmail(String email);
}
