package com.schoolmanagement.repository;

import com.schoolmanagement.model.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByStudentIdAndCourseIdAndSemesterAndAssessmentType(
        Long studentId, Long courseId, String semester, Grade.AssessmentType assessmentType);

    List<Grade> findByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, String semester);
    Page<Grade> findByStudentId(Long studentId, Pageable pageable);
    Page<Grade> findByCourseIdAndSemester(Long courseId, String semester, Pageable pageable);

    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.course.id = :courseId AND g.semester = :semester AND g.assessmentType = :type")
    Optional<BigDecimal> findAverageScoreByCourseAndSemesterAndType(@Param("courseId") Long courseId,
                                                                     @Param("semester") String semester,
                                                                     @Param("type") Grade.AssessmentType type);

    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester ORDER BY g.course.name")
    List<Grade> findTranscriptByStudentAndSemester(@Param("studentId") Long studentId,
                                                   @Param("semester") String semester);

    @Query("SELECT AVG(g.gradePoints) FROM Grade g WHERE g.student.id = :studentId AND g.semester = :semester")
    Optional<BigDecimal> calculateGPAByStudentAndSemester(@Param("studentId") Long studentId,
                                                           @Param("semester") String semester);
}
