package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.AttendanceRequest;
import com.schoolmanagement.dto.response.*;
import com.schoolmanagement.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance Tracking")
@SecurityRequirement(name = "bearerAuth")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Mark student attendance")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(
            @Valid @RequestBody AttendanceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
            attendanceService.markAttendance(request, userDetails.getUsername()), "Attendance marked"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an attendance record")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> updateAttendance(
            @PathVariable Long id, @Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.updateAttendance(id, request)));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get attendance records for a student in a course")
    public ResponseEntity<ApiResponse<PagedResponse<AttendanceResponse>>> getStudentAttendance(
            @PathVariable Long studentId, @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            attendanceService.getStudentAttendance(studentId, courseId, page, size)));
    }

    @GetMapping("/course/{courseId}/date/{date}")
    @Operation(summary = "Get all attendance records for a course on a specific date")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getCourseAttendanceByDate(
            @PathVariable Long courseId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(
            attendanceService.getCourseAttendanceByDate(courseId, date)));
    }

    @GetMapping("/summary/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get attendance summary (percentage) for a student in a course")
    public ResponseEntity<ApiResponse<AttendanceSummaryResponse>> getAttendanceSummary(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceSummary(studentId, courseId)));
    }
}
