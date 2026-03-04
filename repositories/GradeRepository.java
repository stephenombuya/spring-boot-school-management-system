package com.schoolmanagement.repositories;

import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Grade;
import com.schoolmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent(Student student);
    List<Grade> findByCourse(Course course);
    List<Grade> findByStudentAndCourse(Student student, Course course);
    List<Grade> findByAssignmentType(String assignmentType);
    List<Grade> findByStudentAndAssignmentType(Student student, String assignmentType);
    List<Grade> findByCourseAndAssignmentType(Course course, String assignmentType);
}
