package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.RegisterRequest;
import com.schoolmanagement.dto.response.ApiResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.dto.response.UserResponse;
import com.schoolmanagement.model.Role;
import com.schoolmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Manage students, teachers, and admins")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get the currently authenticated user's profile")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user's profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id,
                                                                @Valid @RequestBody RegisterRequest request,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.updateUser(id, request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getById(id)));
    }

    @GetMapping("/students")
    @Operation(summary = "Get all students (paginated)")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllByRole(Role.STUDENT, page, size, search)));
    }

    @GetMapping("/teachers")
    @Operation(summary = "Get all teachers (paginated)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PagedResponse<UserResponse>>> getTeachers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllByRole(Role.TEACHER, page, size, search)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activate or deactivate a user account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(@PathVariable Long id,
                                                          @RequestParam boolean active) {
        userService.toggleUserStatus(id, active);
        String status = active ? "activated" : "deactivated";
        return ResponseEntity.ok(ApiResponse.success(null, "User account " + status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a user account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(userService.getDashboardStats()));
    }
}
