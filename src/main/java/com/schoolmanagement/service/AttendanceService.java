package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.AttendanceRequest;
import com.schoolmanagement.dto.response.AttendanceResponse;
import com.schoolmanagement.dto.response.AttendanceSummaryResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.exception.DuplicateResourceException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.*;
import com.schoolmanagement.repository.AttendanceRepository;
import com.schoolmanagement.repository.CourseRepository;
import com.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public AttendanceResponse markAttendance(AttendanceRequest request, String markedByEmail) {
        if (attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(
                request.getStudentId(), request.getCourseId(), request.getAttendanceDate()).isPresent()) {
            throw new DuplicateResourceException("Attendance already marked for this date");
        }

        User student = findStudent(request.getStudentId());
        Course course = findCourse(request.getCourseId());
        User markedBy = userRepository.findByEmail(markedByEmail).orElseThrow();

        Attendance attendance = Attendance.builder()
            .student(student).course(course)
            .attendanceDate(request.getAttendanceDate())
            .status(request.getStatus())
            .remarks(request.getRemarks())
            .markedBy(markedBy)
            .build();

        return toResponse(attendanceRepository.save(attendance));
    }

    @Transactional
    public AttendanceResponse updateAttendance(Long id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance", "id", id));
        attendance.setStatus(request.getStatus());
        attendance.setRemarks(request.getRemarks());
        return toResponse(attendanceRepository.save(attendance));
    }

    public PagedResponse<AttendanceResponse> getStudentAttendance(Long studentId, Long courseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("attendanceDate").descending());
        Page<Attendance> result = attendanceRepository.findByStudentIdAndCourseId(studentId, courseId, pageable);
        return toPagedResponse(result);
    }

    public List<AttendanceResponse> getCourseAttendanceByDate(Long courseId, LocalDate date) {
        return attendanceRepository.findByCourseIdAndAttendanceDate(courseId, date)
            .stream().map(this::toResponse).toList();
    }

    public AttendanceSummaryResponse getAttendanceSummary(Long studentId, Long courseId) {
        User student = findStudent(studentId);
        Course course = findCourse(courseId);

        long total = attendanceRepository.countTotalByStudentAndCourse(studentId, courseId);
        long present = attendanceRepository.countPresentByStudentAndCourse(studentId, courseId);
        long absent = total - present;
        double percentage = total > 0 ? (present * 100.0 / total) : 0;

        return AttendanceSummaryResponse.builder()
            .studentId(studentId).studentName(student.getFullName())
            .courseId(courseId).courseName(course.getName())
            .totalClasses(total).presentCount(present)
            .absentCount(absent).attendancePercentage(Math.round(percentage * 100.0) / 100.0)
            .build();
    }

    private User findStudent(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    private AttendanceResponse toResponse(Attendance a) {
        return AttendanceResponse.builder()
            .id(a.getId())
            .studentId(a.getStudent().getId()).studentName(a.getStudent().getFullName())
            .courseId(a.getCourse().getId()).courseName(a.getCourse().getName())
            .attendanceDate(a.getAttendanceDate()).status(a.getStatus())
            .remarks(a.getRemarks())
            .markedByName(a.getMarkedBy() != null ? a.getMarkedBy().getFullName() : null)
            .createdAt(a.getCreatedAt())
            .build();
    }

    private PagedResponse<AttendanceResponse> toPagedResponse(Page<Attendance> page) {
        return PagedResponse.<AttendanceResponse>builder()
            .content(page.getContent().stream().map(this::toResponse).toList())
            .page(page.getNumber()).size(page.getSize())
            .totalElements(page.getTotalElements()).totalPages(page.getTotalPages())
            .last(page.isLast()).build();
    }
}
