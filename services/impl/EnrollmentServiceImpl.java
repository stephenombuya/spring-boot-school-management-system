package com.schoolmanagement.service.impl;

import com.schoolmanagement.dto.EnrollmentDTO;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Enrollment;
import com.schoolmanagement.model.Student;
import com.schoolmanagement.repository.CourseRepository;
import com.schoolmanagement.repository.EnrollmentRepository;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EnrollmentDTO> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudent(Student student) {
        return enrollmentRepository.findByStudent(student).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourse(Course course) {
        return enrollmentRepository.findByCourse(course).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudentAndSemester(Student student, String semester, Integer academicYear) {
        return enrollmentRepository.findByStudentAndSemesterAndAcademicYear(student, semester, academicYear).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourseAndSemester(Course course, String semester, Integer academicYear) {
        return enrollmentRepository.findByCourseAndSemesterAndAcademicYear(course, semester, academicYear).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO createEnrollment(Enrollment enrollment) {
        // Make sure student and course exist
        Student student = studentRepository.findById(enrollment.getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + enrollment.getStudent().getId()));
        Course course = courseRepository.findById(enrollment.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + enrollment.getCourse().getId()));
        
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(savedEnrollment);
    }

    @Override
    public EnrollmentDTO updateEnrollment(Long id, Enrollment enrollmentDetails) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        
        // Only update relevant fields
        enrollment.setSemester(enrollmentDetails.getSemester());
        enrollment.setAcademicYear(enrollmentDetails.getAcademicYear());
        enrollment.setStatus(enrollmentDetails.getStatus());
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(updatedEnrollment);
    }

    @Override
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public boolean isStudentEnrolled(Student student, Course course, String semester, Integer academicYear) {
        return enrollmentRepository.existsByStudentAndCourseAndSemesterAndAcademicYear(
                student, course, semester, academicYear);
    }
    
    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setId(enrollment.getId());
        enrollmentDTO.setStudentId(enrollment.getStudent().getId());
        enrollmentDTO.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        enrollmentDTO.setCourseId(enrollment.getCourse().getId());
        enrollmentDTO.setCourseName(enrollment.getCourse().getName());
        enrollmentDTO.setSemester(enrollment.getSemester());
        enrollmentDTO.setAcademicYear(enrollment.getAcademicYear());
        enrollmentDTO.setEnrollmentDate(enrollment.getEnrollmentDate());
        enrollmentDTO.setStatus(enrollment.getStatus());
        return enrollmentDTO;
    }
}
