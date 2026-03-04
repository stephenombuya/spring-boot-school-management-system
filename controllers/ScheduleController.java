package com.schoolmanagement.controllers;

import com.schoolmanagement.dto.ScheduleDTO;
import com.schoolmanagement.model.Schedule;
import com.schoolmanagement.service.ScheduleService;
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
@RequestMapping("/api/schedules")
@Tag(name = "Schedule Management", description = "Operations for managing class schedules")
@SecurityRequirement(name = "bearerAuth")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Retrieve a list of all class schedules")
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully")
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        List<ScheduleDTO> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID", description = "Retrieve a specific schedule by ID")
    @ApiResponse(responseCode = "200", description = "Schedule found")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get schedules by course ID", description = "Retrieve all schedules for a specific course")
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByCourseId(@PathVariable Long courseId) {
        List<ScheduleDTO> schedules = scheduleService.getSchedulesByCourseId(courseId);
        return ResponseEntity.ok(schedules);
    }
    
    @GetMapping("/date")
    @Operation(summary = "Get schedules by date", description = "Retrieve all schedules for a specific date")
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ScheduleDTO> schedules = scheduleService.getSchedulesByDate(date);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Create a new schedule", description = "Create a new class schedule entry")
    @ApiResponse(responseCode = "201", description = "Schedule created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody Schedule schedule) {
        ScheduleDTO createdSchedule = scheduleService.createSchedule(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedule);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Update a schedule", description = "Update schedule information by ID")
    @ApiResponse(responseCode = "200", description = "Schedule updated successfully")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    public ResponseEntity<ScheduleDTO> updateSchedule(@PathVariable Long id, @Valid @RequestBody Schedule schedule) {
        ScheduleDTO updatedSchedule = scheduleService.updateSchedule(id, schedule);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete a schedule", description = "Delete a schedule by ID")
    @ApiResponse(responseCode = "204", description = "Schedule deleted successfully")
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
