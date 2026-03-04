package com.schoolmanagement.dtos;

import com.schoolmanagement.model.Student;
import com.schoolmanagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO extends UserDTO {
    private String studentId;
    private LocalDate dateOfBirth;
    private String guardianName;
    private String guardianContact;
    private Student.GradeLevel gradeLevel;
}
