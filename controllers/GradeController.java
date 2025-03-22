package com.schoolmanagement.controllers;

import com.schoolmanagement.dto.GradeDTO;
import com.schoolmanagement.model.Grade;
import com.schoolmanagement.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade Management", description = "Operations for managing student grades")
@SecurityRequirement(name = "bearerAuth")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @userSecurity.isCurrentUser(#studentId)")
    @Operation(summary = "Get grades by student ID", description = "Retrieve grades for a specific student")
    @ApiResponse(responseCode = "200", description = "List of grades retrieved successfully")
    public ResponseEntity<List<GradeDTO>> getGradesByStudentId(@PathVariable Long studentId) {
        List<GradeDTO> grades = gradeService.getGradesByStudentId(studentId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get grades by course ID", description = "Retrieve grades for a specific course")
    @ApiResponse(responseCode = "200", description = "List of grades retrieved successfully")
    public ResponseEntity<List<GradeDTO>> getGradesByCourseId(@PathVariable Long courseId) {
        List<GradeDTO> grades = gradeService.getGradesByCourseId(courseId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/enrollment/{enrollmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @enrollmentSecurity.isEnrolledStudent(#enrollmentId)")
    @Operation(summary = "Get grades by enrollment ID", description = "Retrieve grades for a specific enrollment")
    @ApiResponse(responseCode = "200", description = "List of grades retrieved successfully")
    public ResponseEntity<List<GradeDTO>> getGradesByEnrollmentId(@PathVariable Long enrollmentId) {
        List<GradeDTO> grades = gradeService.getGradesByEnrollmentId(enrollmentId);
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a grade", description = "Create a new grade entry")
    @ApiResponse(responseCode = "201", description = "Grade created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<GradeDTO> createGrade(@Valid @RequestBody Grade grade) {
        GradeDTO createdGrade = gradeService.createGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
    }
    
    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create multiple grades", description = "Create multiple grade entries at once")
    @ApiResponse(responseCode = "201", description = "Grades created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<List<GradeDTO>> createBatchGrades(@Valid @RequestBody List<Grade> grades) {
        List<GradeDTO> createdGrades = gradeService.createBatchGrades(grades);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrades);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update a grade", description = "Update a grade entry by ID")
    @ApiResponse(responseCode = "200", description = "Grade updated successfully")
    @ApiResponse(responseCode = "404", description = "Grade not found")
    public ResponseEntity<GradeDTO> updateGrade(@PathVariable Long id, @Valid @RequestBody Grade grade) {
        GradeDTO updatedGrade = gradeService.updateGrade(id, grade);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete a grade", description = "Delete a grade entry by ID")
    @ApiResponse(responseCode = "204", description = "Grade deleted successfully")
    @ApiResponse(responseCode = "404", description = "Grade not found")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
