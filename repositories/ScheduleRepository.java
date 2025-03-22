package com.schoolmanagement.repositories;

import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourse(Course course);
    List<Schedule> findByType(Schedule.ScheduleType type);
    List<Schedule> findByDay(DayOfWeek day);
    List<Schedule> findByDate(LocalDate date);
    List<Schedule> findByCourseAndType(Course course, Schedule.ScheduleType type);
}
