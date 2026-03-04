package com.schoolmanagement.repository;

import com.schoolmanagement.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseId(Long courseId);
    Page<Schedule> findByCourseIdAndEventType(Long courseId, Schedule.ScheduleType type, Pageable pageable);

    @Query("SELECT s FROM Schedule s WHERE s.course.id IN " +
           "(SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ACTIVE') " +
           "AND s.startTime >= :from AND s.startTime <= :to ORDER BY s.startTime")
    List<Schedule> findStudentSchedule(@Param("studentId") Long studentId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    @Query("SELECT s FROM Schedule s WHERE s.course.teacher.id = :teacherId " +
           "AND s.startTime >= :from AND s.startTime <= :to ORDER BY s.startTime")
    List<Schedule> findTeacherSchedule(@Param("teacherId") Long teacherId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to);

    @Query("SELECT s FROM Schedule s WHERE s.startTime >= :from AND s.startTime <= :to " +
           "AND s.eventType = :type ORDER BY s.startTime")
    List<Schedule> findUpcomingByType(@Param("type") Schedule.ScheduleType type,
                                      @Param("from") LocalDateTime from,
                                      @Param("to") LocalDateTime to);
}
