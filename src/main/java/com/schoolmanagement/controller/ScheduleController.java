package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.ScheduleRequest;
import com.schoolmanagement.dto.response.ApiResponse;
import com.schoolmanagement.dto.response.ScheduleResponse;
import com.schoolmanagement.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule Management")
@SecurityRequirement(name = "bearerAuth")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a new schedule event")
    public ResponseEntity<ApiResponse<ScheduleResponse>> create(@Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(scheduleService.createSchedule(request), "Schedule created"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update a schedule event")
    public ResponseEntity<ApiResponse<ScheduleResponse>> update(@PathVariable Long id,
                                                                @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(scheduleService.updateSchedule(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Delete a schedule event")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Schedule deleted"));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all schedules for a course")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getCourseSchedule(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success(scheduleService.getCourseSchedule(courseId)));
    }

    @GetMapping("/student/{studentId}/timetable")
    @Operation(summary = "Get student timetable for a date range")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getStudentTimetable(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(scheduleService.getStudentTimetable(studentId, from, to)));
    }

    @GetMapping("/teacher/{teacherId}/timetable")
    @Operation(summary = "Get teacher timetable for a date range")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getTeacherTimetable(
            @PathVariable Long teacherId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(scheduleService.getTeacherTimetable(teacherId, from, to)));
    }

    @GetMapping("/exams/upcoming")
    @Operation(summary = "Get upcoming exams within a date range")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getUpcomingExams(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(ApiResponse.success(scheduleService.getUpcomingExams(from, to)));
    }
}
