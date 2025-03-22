package com.schoolmanagement.service.impl;

import com.schoolmanagement.dto.StudentDTO;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Student;
import com.schoolmanagement.model.User;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDTO> getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<StudentDTO> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<StudentDTO> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Override
    public StudentDTO createStudent(Student student) {
        // Set role to STUDENT
        student.setRole(User.Role.STUDENT);
        // Encrypt password before saving
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setEmail(studentDetails.getEmail());
        student.setPhoneNumber(studentDetails.getPhoneNumber());
        student.setAddress(studentDetails.getAddress());
        
        // Only update password if it's provided
        if (studentDetails.getPassword() != null && !studentDetails.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(studentDetails.getPassword()));
        }
        
        student.setActive(studentDetails.isActive());
        student.setStudentId(studentDetails.getStudentId());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setGuardianName(studentDetails.getGuardianName());
        student.setGuardianContact(studentDetails.getGuardianContact());
        student.setGradeLevel(studentDetails.getGradeLevel());
        
        Student updatedStudent = studentRepository.save(student);
        return convertToDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    @Override
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    private StudentDTO convertToDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPhoneNumber(student.getPhoneNumber());
        studentDTO.setAddress(student.getAddress());
        studentDTO.setRole(student.getRole());
        studentDTO.setActive(student.isActive());
        studentDTO.setStudentId(student.getStudentId());
        studentDTO.setDateOfBirth(student.getDateOfBirth());
        studentDTO.setGuardianName(student.getGuardianName());
        studentDTO.setGuardianContact(student.getGuardianContact());
        studentDTO.setGradeLevel(student.getGradeLevel());
        return studentDTO;
    }
}
