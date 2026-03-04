package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.EnrollmentRequest;
import com.schoolmanagement.dto.response.ApiResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.model.Enrollment;
import com.schoolmanagement.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollment Management")
@SecurityRequirement(name = "bearerAuth")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll a student in a course")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Enrollment>> enroll(@Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(enrollmentService.enroll(request), "Enrollment successful"));
    }

    @PatchMapping("/{id}/drop")
    @Operation(summary = "Drop a course enrollment")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<Enrollment>> dropCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.dropCourse(id), "Course dropped successfully"));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all enrollments for a student")
    public ResponseEntity<ApiResponse<PagedResponse<Enrollment>>> getStudentEnrollments(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getStudentEnrollments(studentId, page, size)));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all enrollments for a course")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<PagedResponse<Enrollment>>> getCourseEnrollments(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getCourseEnrollments(courseId, page, size)));
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    @Operation(summary = "Get student's course summary for a semester")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSemesterSummary(
            @PathVariable Long studentId, @PathVariable String semester) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getStudentSemesterSummary(studentId, semester)));
    }
}
