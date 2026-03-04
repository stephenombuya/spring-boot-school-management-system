package com.schoolmanagement.repository;

import com.schoolmanagement.model.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, String semester);
    boolean existsByStudentIdAndCourseIdAndSemester(Long studentId, Long courseId, String semester);
    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);
    Page<Enrollment> findByCourseId(Long courseId, Pageable pageable);
    List<Enrollment> findByStudentIdAndSemester(Long studentId, String semester);

    @Query("SELECT e FROM Enrollment e WHERE e.course.teacher.id = :teacherId AND e.semester = :semester")
    List<Enrollment> findByTeacherIdAndSemester(@Param("teacherId") Long teacherId,
                                                @Param("semester") String semester);
}
