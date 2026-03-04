package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.ScheduleRequest;
import com.schoolmanagement.dto.response.ScheduleResponse;
import com.schoolmanagement.exception.BusinessException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Schedule;
import com.schoolmanagement.repository.CourseRepository;
import com.schoolmanagement.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new BusinessException("End time must be after start time");
        }

        Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        Schedule schedule = Schedule.builder()
            .course(course)
            .title(request.getTitle())
            .description(request.getDescription())
            .eventType(request.getEventType())
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .location(request.getLocation())
            .isRecurring(request.isRecurring())
            .recurrencePattern(request.getRecurrencePattern())
            .build();

        return toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    public ScheduleResponse updateSchedule(Long id, ScheduleRequest request) {
        Schedule schedule = findById(id);
        schedule.setTitle(request.getTitle());
        schedule.setDescription(request.getDescription());
        schedule.setEventType(request.getEventType());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setLocation(request.getLocation());
        return toResponse(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.delete(findById(id));
    }

    public List<ScheduleResponse> getCourseSchedule(Long courseId) {
        return scheduleRepository.findByCourseId(courseId)
            .stream().map(this::toResponse).toList();
    }

    public List<ScheduleResponse> getStudentTimetable(Long studentId, LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.findStudentSchedule(studentId, from, to)
            .stream().map(this::toResponse).toList();
    }

    public List<ScheduleResponse> getTeacherTimetable(Long teacherId, LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.findTeacherSchedule(teacherId, from, to)
            .stream().map(this::toResponse).toList();
    }

    public List<ScheduleResponse> getUpcomingExams(LocalDateTime from, LocalDateTime to) {
        return scheduleRepository.findUpcomingByType(Schedule.ScheduleType.EXAM, from, to)
            .stream().map(this::toResponse).toList();
    }

    private Schedule findById(Long id) {
        return scheduleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
    }

    private ScheduleResponse toResponse(Schedule s) {
        return ScheduleResponse.builder()
            .id(s.getId())
            .courseId(s.getCourse().getId())
            .courseName(s.getCourse().getName())
            .courseCode(s.getCourse().getCourseCode())
            .title(s.getTitle()).description(s.getDescription())
            .eventType(s.getEventType())
            .startTime(s.getStartTime()).endTime(s.getEndTime())
            .location(s.getLocation())
            .recurring(s.isRecurring())
            .recurrencePattern(s.getRecurrencePattern())
            .build();
    }
}
