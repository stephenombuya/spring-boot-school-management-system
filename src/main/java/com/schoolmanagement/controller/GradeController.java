package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.GradeRequest;
import com.schoolmanagement.dto.response.*;
import com.schoolmanagement.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
@Tag(name = "Grading System")
@SecurityRequirement(name = "bearerAuth")
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Submit a grade for a student")
    public ResponseEntity<ApiResponse<GradeResponse>> submitGrade(
            @Valid @RequestBody GradeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(gradeService.submitGrade(request, userDetails.getUsername()), "Grade submitted"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update a grade")
    public ResponseEntity<ApiResponse<GradeResponse>> updateGrade(@PathVariable Long id,
                                                                   @Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.updateGrade(id, request), "Grade updated"));
    }

    @GetMapping("/student/{studentId}/course/{courseId}/semester/{semester}")
    @Operation(summary = "Get all grades for a student in a course")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getStudentGrades(
            @PathVariable Long studentId, @PathVariable Long courseId, @PathVariable String semester) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getStudentGrades(studentId, courseId, semester)));
    }

    @GetMapping("/course/{courseId}/semester/{semester}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get all grades for a course in a semester")
    public ResponseEntity<ApiResponse<PagedResponse<GradeResponse>>> getCourseGrades(
            @PathVariable Long courseId, @PathVariable String semester,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getCourseGrades(courseId, semester, page, size)));
    }

    @GetMapping("/transcript/student/{studentId}/semester/{semester}")
    @Operation(summary = "Get a student's full transcript for a semester")
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getTranscript(
            @PathVariable Long studentId, @PathVariable String semester) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getStudentTranscript(studentId, semester)));
    }

    @GetMapping("/gpa/student/{studentId}/semester/{semester}")
    @Operation(summary = "Get a student's GPA for a semester")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGPA(
            @PathVariable Long studentId, @PathVariable String semester) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getStudentGPASummary(studentId, semester)));
    }
}
