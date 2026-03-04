package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.CourseRequest;
import com.schoolmanagement.dto.response.ApiResponse;
import com.schoolmanagement.dto.response.CourseResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.service.CourseService;
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

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "Create and manage academic courses")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all active courses (paginated)")
    public ResponseEntity<ApiResponse<PagedResponse<CourseResponse>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getAllCourses(page, size, search)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a course by ID")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCourseById(id)));
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get all courses taught by a teacher")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCoursesByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCoursesByTeacher(teacherId)));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all courses a student is enrolled in")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getCoursesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(courseService.getCoursesByStudent(studentId)));
    }

    @PostMapping
    @Operation(summary = "Create a new course")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Course created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a course")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@PathVariable Long id,
                                                                    @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(courseService.updateCourse(id, request), "Course updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate a course (soft delete)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Course deactivated successfully"));
    }
}
