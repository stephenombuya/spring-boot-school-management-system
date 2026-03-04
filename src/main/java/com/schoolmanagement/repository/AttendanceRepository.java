package com.schoolmanagement.repository;

import com.schoolmanagement.model.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentIdAndCourseIdAndAttendanceDate(Long studentId, Long courseId, LocalDate date);
    Page<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId, Pageable pageable);
    List<Attendance> findByCourseIdAndAttendanceDate(Long courseId, LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId AND a.status = 'PRESENT'")
    long countPresentByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId")
    long countTotalByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query("SELECT a FROM Attendance a WHERE a.course.id = :courseId AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<Attendance> findByCourseAndDateRange(@Param("courseId") Long courseId,
                                              @Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);
}
