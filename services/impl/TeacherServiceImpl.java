package com.schoolmanagement.service.impl;

import com.schoolmanagement.dto.TeacherDTO;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Teacher;
import com.schoolmanagement.model.User;
import com.schoolmanagement.repository.TeacherRepository;
import com.schoolmanagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TeacherDTO> getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<TeacherDTO> getTeacherByEmployeeId(String employeeId) {
        return teacherRepository.findByEmployeeId(employeeId)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<TeacherDTO> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    public List<TeacherDTO> getTeachersByDepartment(String department) {
        return teacherRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherDTO createTeacher(Teacher teacher) {
        // Set role to TEACHER
        teacher.setRole(User.Role.TEACHER);
        // Encrypt password before saving
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDTO(savedTeacher);
    }

    @Override
    public TeacherDTO updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        
        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setEmail(teacherDetails.getEmail());
        teacher.setPhoneNumber(teacherDetails.getPhoneNumber());
        teacher.setAddress(teacherDetails.getAddress());
        
        // Only update password if it's provided
        if (teacherDetails.getPassword() != null && !teacherDetails.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(teacherDetails.getPassword()));
        }
        
        teacher.setActive(teacherDetails.isActive());
        teacher.setEmployeeId(teacherDetails.getEmployeeId());
        teacher.setDepartment(teacherDetails.getDepartment());
        teacher.setSpecialization(teacherDetails.getSpecialization());
        teacher.setQualification(teacherDetails.getQualification());
        teacher.setYearsOfExperience(teacherDetails.getYearsOfExperience());
        
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return convertToDTO(updatedTeacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
        teacherRepository.delete(teacher);
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) {
        return teacherRepository.existsByEmployeeId(employeeId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }
    
    private TeacherDTO convertToDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setFirstName(teacher.getFirstName());
        teacherDTO.setLastName(teacher.getLastName());
        teacherDTO.setEmail(teacher.getEmail());
        teacherDTO.setPhoneNumber(teacher.getPhoneNumber());
        teacherDTO.setAddress(teacher.getAddress());
        teacherDTO.setRole(teacher.getRole());
        teacherDTO.setActive(teacher.isActive());
        teacherDTO.setEmployeeId(teacher.getEmployeeId());
        teacherDTO.setDepartment(teacher.getDepartment());
        teacherDTO.setSpecialization(teacher.getSpecialization());
        teacherDTO.setQualification(teacher.getQualification());
        teacherDTO.setYearsOfExperience(teacher.getYearsOfExperience());
        return teacherDTO;
    }
}
