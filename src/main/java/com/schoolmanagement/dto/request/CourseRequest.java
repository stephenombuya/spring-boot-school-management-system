package com.schoolmanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseRequest {
    @NotBlank(message = "Course name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Course code is required")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z]{2,5}[0-9]{3,4}$", message = "Course code format: e.g. CS101, MATH301")
    private String courseCode;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Credits are required")
    @Min(1) @Max(6)
    private Integer credits;

    @Min(1) @Max(200)
    private Integer maxCapacity;

    private Long teacherId;
}
