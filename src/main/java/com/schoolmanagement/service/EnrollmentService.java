package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.EnrollmentRequest;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.exception.BusinessException;
import com.schoolmanagement.exception.DuplicateResourceException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.*;
import com.schoolmanagement.repository.CourseRepository;
import com.schoolmanagement.repository.EnrollmentRepository;
import com.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Enrollment enroll(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndSemester(
                request.getStudentId(), request.getCourseId(), request.getSemester())) {
            throw new DuplicateResourceException("Student is already enrolled in this course for " + request.getSemester());
        }

        User student = userRepository.findById(request.getStudentId())
            .filter(u -> u.getRole().equals(Role.STUDENT))
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Course course = courseRepository.findById(request.getCourseId())
            .filter(Course::isActive)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        long currentEnrollments = courseRepository.countActiveEnrollments(course.getId());
        if (currentEnrollments >= course.getMaxCapacity()) {
            throw new BusinessException("Course " + course.getCourseCode() + " is at full capacity");
        }

        Enrollment enrollment = Enrollment.builder()
            .student(student)
            .course(course)
            .semester(request.getSemester())
            .status(Enrollment.EnrollmentStatus.ACTIVE)
            .enrollmentDate(LocalDate.now())
            .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("Student {} enrolled in course {} for {}", student.getEmail(), course.getCourseCode(), request.getSemester());
        return saved;
    }

    @Transactional
    public Enrollment dropCourse(Long enrollmentId) {
        Enrollment enrollment = findById(enrollmentId);
        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.DROPPED) {
            throw new BusinessException("Student is not currently enrolled in this course");
        }
        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        enrollment.setDropDate(LocalDate.now());
        return enrollmentRepository.save(enrollment);
    }

    public PagedResponse<Enrollment> getStudentEnrollments(Long studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("enrollmentDate").descending());
        Page<Enrollment> result = enrollmentRepository.findByStudentId(studentId, pageable);
        return PagedResponse.<Enrollment>builder()
            .content(result.getContent()).page(result.getNumber()).size(result.getSize())
            .totalElements(result.getTotalElements()).totalPages(result.getTotalPages())
            .last(result.isLast()).build();
    }

    public PagedResponse<Enrollment> getCourseEnrollments(Long courseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("enrollmentDate").descending());
        Page<Enrollment> result = enrollmentRepository.findByCourseId(courseId, pageable);
        return PagedResponse.<Enrollment>builder()
            .content(result.getContent()).page(result.getNumber()).size(result.getSize())
            .totalElements(result.getTotalElements()).totalPages(result.getTotalPages())
            .last(result.isLast()).build();
    }

    public List<Map<String, Object>> getStudentSemesterSummary(Long studentId, String semester) {
        return enrollmentRepository.findByStudentIdAndSemester(studentId, semester)
            .stream()
            .map(e -> Map.<String, Object>of(
                "courseId", e.getCourse().getId(),
                "courseName", e.getCourse().getName(),
                "courseCode", e.getCourse().getCourseCode(),
                "credits", e.getCourse().getCredits(),
                "status", e.getStatus()
            )).toList();
    }

    private Enrollment findById(Long id) {
        return enrollmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", id));
    }
}
