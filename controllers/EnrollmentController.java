package com.schoolmanagement.controllers;

import com.schoolmanagement.dto.EnrollmentDTO;
import com.schoolmanagement.model.Enrollment;
import com.schoolmanagement.service.EnrollmentService;
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
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollment Management", description = "Operations for managing course enrollments")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all enrollments", description = "Retrieve a list of all enrollments")
    @ApiResponse(responseCode = "200", description = "List of enrollments retrieved successfully")
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @enrollmentSecurity.isEnrolledStudent(#id)")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve a specific enrollment by ID")
    @ApiResponse(responseCode = "200", description = "Enrollment found")
    @ApiResponse(responseCode = "404", description = "Enrollment not found")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @userSecurity.isCurrentUser(#studentId)")
    @Operation(summary = "Get enrollments by student ID", description = "Retrieve all enrollments for a specific student")
    @ApiResponse(responseCode = "200", description = "List of enrollments retrieved successfully")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get enrollments by course ID", description = "Retrieve all enrollments for a specific course")
    @ApiResponse(responseCode = "200", description = "List of enrollments retrieved successfully")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new enrollment", description = "Enroll a student in a course")
    @ApiResponse(responseCode = "201", description = "Enrollment created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<EnrollmentDTO> createEnrollment(@Valid @RequestBody Enrollment enrollment) {
        EnrollmentDTO createdEnrollment = enrollmentService.createEnrollment(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update an enrollment", description = "Update enrollment information by ID")
    @ApiResponse(responseCode = "200", description = "Enrollment updated successfully")
    @ApiResponse(responseCode = "404", description = "Enrollment not found")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable Long id, @Valid @RequestBody Enrollment enrollment) {
        EnrollmentDTO updatedEnrollment = enrollmentService.updateEnrollment(id, enrollment);
        return ResponseEntity.ok(updatedEnrollment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an enrollment", description = "Delete an enrollment by ID")
    @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully")
    @ApiResponse(responseCode = "404", description = "Enrollment not found")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
