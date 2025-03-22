package com.schoolmanagement.repositories;

import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Enrollment;
import com.schoolmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByCourse(Course course);
    List<Enrollment> findByStudentAndSemesterAndAcademicYear(
            Student student, String semester, Integer academicYear);
    List<Enrollment> findByCourseAndSemesterAndAcademicYear(
            Course course, String semester, Integer academicYear);
    boolean existsByStudentAndCourseAndSemesterAndAcademicYear(
            Student student, Course course, String semester, Integer academicYear);
}
