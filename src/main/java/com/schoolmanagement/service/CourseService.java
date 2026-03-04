package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.CourseRequest;
import com.schoolmanagement.dto.response.CourseResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.exception.BusinessException;
import com.schoolmanagement.exception.DuplicateResourceException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Course;
import com.schoolmanagement.model.Role;
import com.schoolmanagement.model.User;
import com.schoolmanagement.repository.CourseRepository;
import com.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public PagedResponse<CourseResponse> getAllCourses(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Course> courses = (search != null && !search.isBlank())
            ? courseRepository.searchActiveCourses(search, pageable)
            : courseRepository.findByIsActive(true, pageable);
        return toPagedResponse(courses);
    }

    public CourseResponse getCourseById(Long id) {
        return toResponse(findById(id));
    }

    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId)
            .stream().map(this::toResponse).toList();
    }

    public List<CourseResponse> getCoursesByStudent(Long studentId) {
        return courseRepository.findCoursesByStudentId(studentId)
            .stream().map(this::toResponse).toList();
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course code already exists: " + request.getCourseCode());
        }

        Course course = Course.builder()
            .name(request.getName())
            .courseCode(request.getCourseCode())
            .description(request.getDescription())
            .credits(request.getCredits())
            .maxCapacity(request.getMaxCapacity() != null ? request.getMaxCapacity() : 30)
            .isActive(true)
            .build();

        if (request.getTeacherId() != null) {
            User teacher = userRepository.findById(request.getTeacherId())
                .filter(u -> u.getRole().equals(Role.TEACHER))
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", request.getTeacherId()));
            course.setTeacher(teacher);
        }

        Course saved = courseRepository.save(course);
        log.info("Course created: {} ({})", saved.getName(), saved.getCourseCode());
        return toResponse(saved);
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = findById(id);

        if (!course.getCourseCode().equals(request.getCourseCode()) &&
            courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course code already in use: " + request.getCourseCode());
        }

        course.setName(request.getName());
        course.setCourseCode(request.getCourseCode());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        if (request.getMaxCapacity() != null) course.setMaxCapacity(request.getMaxCapacity());

        if (request.getTeacherId() != null) {
            User teacher = userRepository.findById(request.getTeacherId())
                .filter(u -> u.getRole().equals(Role.TEACHER))
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", request.getTeacherId()));
            course.setTeacher(teacher);
        } else {
            course.setTeacher(null);
        }

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = findById(id);
        long enrolled = courseRepository.countActiveEnrollments(id);
        if (enrolled > 0) {
            throw new BusinessException("Cannot delete a course with " + enrolled + " active enrollments");
        }
        course.setActive(false);
        courseRepository.save(course);
    }

    private Course findById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
    }

    private CourseResponse toResponse(Course c) {
        long enrolled = courseRepository.countActiveEnrollments(c.getId());
        return CourseResponse.builder()
            .id(c.getId())
            .name(c.getName())
            .courseCode(c.getCourseCode())
            .description(c.getDescription())
            .credits(c.getCredits())
            .maxCapacity(c.getMaxCapacity())
            .currentEnrollments(enrolled)
            .active(c.isActive())
            .teacherId(c.getTeacher() != null ? c.getTeacher().getId() : null)
            .teacherName(c.getTeacher() != null ? c.getTeacher().getFullName() : null)
            .createdAt(c.getCreatedAt())
            .build();
    }

    private PagedResponse<CourseResponse> toPagedResponse(Page<Course> page) {
        return PagedResponse.<CourseResponse>builder()
            .content(page.getContent().stream().map(this::toResponse).toList())
            .page(page.getNumber()).size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .last(page.isLast()).build();
    }
}
