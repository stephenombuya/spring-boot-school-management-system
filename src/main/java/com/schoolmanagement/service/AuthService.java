package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.LoginRequest;
import com.schoolmanagement.dto.request.RegisterRequest;
import com.schoolmanagement.dto.response.AuthResponse;
import com.schoolmanagement.exception.DuplicateResourceException;
import com.schoolmanagement.model.User;
import com.schoolmanagement.repository.UserRepository;
import com.schoolmanagement.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }

        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .phone(request.getPhone())
            .address(request.getAddress())
            .dateOfBirth(request.getDateOfBirth())
            .isActive(true)
            .build();

        userRepository.save(user);
        log.info("Registered new {} with email: {}", request.getRole(), request.getEmail());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtils.generateToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        String accessToken = jwtUtils.generateToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String username = jwtUtils.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtils.isTokenValid(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(username).orElseThrow();
        String newAccessToken = jwtUtils.generateToken(userDetails);
        String newRefreshToken = jwtUtils.generateRefreshToken(userDetails);

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(86400L)
            .userId(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .role(user.getRole())
            .build();
    }
}
