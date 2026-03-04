package com.schoolmanagement.controllers;

import com.schoolmanagement.dto.AttendanceDTO;
import com.schoolmanagement.model.Attendance;
import com.schoolmanagement.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance Management", description = "Operations for managing student attendance")
@SecurityRequirement(name = "bearerAuth")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @userSecurity.isCurrentUser(#studentId)")
    @Operation(summary = "Get attendance by student ID", description = "Retrieve attendance records for a specific student")
    @ApiResponse(responseCode = "200", description = "List of attendance records retrieved successfully")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable Long studentId) {
        List<AttendanceDTO> attendanceRecords = attendanceService.getAttendanceByStudentId(studentId);
        return ResponseEntity.ok(attendanceRecords);
    }
    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get attendance by course ID", description = "Retrieve attendance records for a specific course")
    @ApiResponse(responseCode = "200", description = "List of attendance records retrieved successfully")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByCourseId(@PathVariable Long courseId) {
        List<AttendanceDTO> attendanceRecords = attendanceService.getAttendanceByCourseId(courseId);
        return ResponseEntity.ok(attendanceRecords);
    }
    
    @GetMapping("/date")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get attendance by date", description = "Retrieve attendance records for a specific date")
    @ApiResponse(responseCode = "200", description = "List of attendance records retrieved successfully")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceDTO> attendanceRecords = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendanceRecords);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create attendance record", description = "Create a new attendance record")
    @ApiResponse(responseCode = "201", description = "Attendance record created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<AttendanceDTO> createAttendance(@Valid @RequestBody Attendance attendance) {
        AttendanceDTO createdAttendance = attendanceService.createAttendance(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendance);
    }
    
    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create multiple attendance records", description = "Create multiple attendance records at once")
    @ApiResponse(responseCode = "201", description = "Attendance records created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<List<AttendanceDTO>> createBatchAttendance(@Valid @RequestBody List<Attendance> attendances) {
        List<AttendanceDTO> createdAttendances = attendanceService.createBatchAttendance(attendances);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendances);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update attendance record", description = "Update an attendance record by ID")
    @ApiResponse(responseCode = "200", description = "Attendance record updated successfully")
    @ApiResponse(responseCode = "404", description = "Attendance record not found")
    public ResponseEntity<AttendanceDTO> updateAttendance(@PathVariable Long id, @Valid @RequestBody Attendance attendance) {
        AttendanceDTO updatedAttendance = attendanceService.updateAttendance(id, attendance);
        return ResponseEntity.ok(updatedAttendance);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete attendance record", description = "Delete an attendance record by ID")
    @ApiResponse(responseCode = "204", description = "Attendance record deleted successfully")
    @ApiResponse(responseCode = "404", description = "Attendance record not found")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
