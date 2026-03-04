package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.RegisterRequest;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.dto.response.UserResponse;
import com.schoolmanagement.exception.AccessDeniedException;
import com.schoolmanagement.exception.DuplicateResourceException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Role;
import com.schoolmanagement.model.User;
import com.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PagedResponse<UserResponse> getAllByRole(Role role, int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName", "firstName"));
        Page<User> users = (search != null && !search.isBlank())
            ? userRepository.searchByRoleAndName(role, search, pageable)
            : userRepository.findByRole(role, pageable);
        return toPagedResponse(users);
    }

    public UserResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, RegisterRequest request, String currentUserEmail) {
        User user = findById(id);
        User currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow();

        // Only admin can update others; users can only update themselves
        if (!currentUser.getRole().equals(Role.ADMIN) && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You are not authorized to update this user");
        }

        // Check email uniqueness if changed
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void toggleUserStatus(Long id, boolean active) {
        User user = findById(id);
        user.setActive(active);
        userRepository.save(user);
        log.info("User {} status set to {}", id, active ? "active" : "inactive");
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        user.setActive(false); // Soft delete
        userRepository.save(user);
        log.info("User {} soft deleted", id);
    }

    public Map<String, Long> getDashboardStats() {
        return Map.of(
            "students", userRepository.countActiveByRole(Role.STUDENT),
            "teachers", userRepository.countActiveByRole(Role.TEACHER),
            "admins", userRepository.countActiveByRole(Role.ADMIN)
        );
    }

    private User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .role(user.getRole())
            .phone(user.getPhone())
            .address(user.getAddress())
            .dateOfBirth(user.getDateOfBirth())
            .profilePictureUrl(user.getProfilePictureUrl())
            .active(user.isActive())
            .createdAt(user.getCreatedAt())
            .build();
    }

    private PagedResponse<UserResponse> toPagedResponse(Page<User> page) {
        return PagedResponse.<UserResponse>builder()
            .content(page.getContent().stream().map(UserService::toResponse).toList())
            .page(page.getNumber())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .last(page.isLast())
            .build();
    }
}
