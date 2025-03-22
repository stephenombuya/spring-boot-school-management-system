package com.schoolmanagement.repositories;

import com.schoolmanagement.model.Attendance;
import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent(Student student);
    List<Attendance> findByCourse(Course course);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByStudentAndCourse(Student student, Course course);
    List<Attendance> findByStudentAndDate(Student student, LocalDate date);
    List<Attendance> findByCourseAndDate(Course course, LocalDate date);
    boolean existsByStudentAndCourseAndDate(Student student, Course course, LocalDate date);
}
