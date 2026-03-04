package com.schoolmanagement.dto.response;

import com.schoolmanagement.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Role role;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String profilePictureUrl;
    private boolean active;
    private LocalDateTime createdAt;
}
