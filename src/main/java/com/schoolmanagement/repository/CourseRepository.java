package com.schoolmanagement.repository;

import com.schoolmanagement.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    boolean existsByCourseCode(String courseCode);
    Page<Course> findByIsActive(boolean isActive, Pageable pageable);
    List<Course> findByTeacherId(Long teacherId);

    @Query("SELECT c FROM Course c WHERE c.isActive = true AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Course> searchActiveCourses(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.student.id = :studentId AND e.status = 'ACTIVE'")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    long countActiveEnrollments(@Param("courseId") Long courseId);
}
