package com.schoolmanagement.dto.request;

import com.schoolmanagement.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 40, message = "Password must be 8-40 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
             message = "Password must contain uppercase, lowercase, and a digit")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    @Size(max = 20)
    @Pattern(regexp = "^[+]?[0-9]{7,15}$", message = "Invalid phone number")
    private String phone;

    @Size(max = 255)
    private String address;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
