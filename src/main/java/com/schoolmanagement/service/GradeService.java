package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.GradeRequest;
import com.schoolmanagement.dto.response.GradeResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.exception.DuplicateResourceException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.*;
import com.schoolmanagement.repository.CourseRepository;
import com.schoolmanagement.repository.GradeRepository;
import com.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public GradeResponse submitGrade(GradeRequest request, String gradedByEmail) {
        if (gradeRepository.findByStudentIdAndCourseIdAndSemesterAndAssessmentType(
                request.getStudentId(), request.getCourseId(), request.getSemester(),
                request.getAssessmentType()).isPresent()) {
            throw new DuplicateResourceException("Grade already exists for this assessment. Use update instead.");
        }

        User student = userRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
        User gradedBy = userRepository.findByEmail(gradedByEmail).orElseThrow();

        Grade grade = Grade.builder()
            .student(student).course(course)
            .semester(request.getSemester())
            .assessmentType(request.getAssessmentType())
            .score(request.getScore())
            .maxScore(request.getMaxScore() != null ? request.getMaxScore() : BigDecimal.valueOf(100))
            .weightPercentage(request.getWeightPercentage())
            .comments(request.getComments())
            .gradedBy(gradedBy)
            .build();

        return toResponse(gradeRepository.save(grade));
    }

    @Transactional
    public GradeResponse updateGrade(Long id, GradeRequest request) {
        Grade grade = gradeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", id));
        grade.setScore(request.getScore());
        if (request.getMaxScore() != null) grade.setMaxScore(request.getMaxScore());
        grade.setComments(request.getComments());
        if (request.getWeightPercentage() != null) grade.setWeightPercentage(request.getWeightPercentage());
        return toResponse(gradeRepository.save(grade));
    }

    public List<GradeResponse> getStudentGrades(Long studentId, Long courseId, String semester) {
        return gradeRepository.findByStudentIdAndCourseIdAndSemester(studentId, courseId, semester)
            .stream().map(this::toResponse).toList();
    }

    public PagedResponse<GradeResponse> getCourseGrades(Long courseId, String semester, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("student.lastName"));
        Page<Grade> result = gradeRepository.findByCourseIdAndSemester(courseId, semester, pageable);
        return PagedResponse.<GradeResponse>builder()
            .content(result.getContent().stream().map(this::toResponse).toList())
            .page(result.getNumber()).size(result.getSize())
            .totalElements(result.getTotalElements()).totalPages(result.getTotalPages())
            .last(result.isLast()).build();
    }

    public List<GradeResponse> getStudentTranscript(Long studentId, String semester) {
        return gradeRepository.findTranscriptByStudentAndSemester(studentId, semester)
            .stream().map(this::toResponse).toList();
    }

    public Map<String, Object> getStudentGPASummary(Long studentId, String semester) {
        BigDecimal gpa = gradeRepository.calculateGPAByStudentAndSemester(studentId, semester)
            .orElse(BigDecimal.ZERO);
        List<GradeResponse> grades = getStudentTranscript(studentId, semester);
        return Map.of("studentId", studentId, "semester", semester, "gpa", gpa, "grades", grades);
    }

    private GradeResponse toResponse(Grade g) {
        return GradeResponse.builder()
            .id(g.getId())
            .studentId(g.getStudent().getId()).studentName(g.getStudent().getFullName())
            .courseId(g.getCourse().getId()).courseName(g.getCourse().getName())
            .semester(g.getSemester()).assessmentType(g.getAssessmentType())
            .score(g.getScore()).maxScore(g.getMaxScore())
            .weightPercentage(g.getWeightPercentage())
            .letterGrade(g.getLetterGrade()).gradePoints(g.getGradePoints())
            .comments(g.getComments())
            .gradedByName(g.getGradedBy() != null ? g.getGradedBy().getFullName() : null)
            .createdAt(g.getCreatedAt())
            .build();
    }
}
