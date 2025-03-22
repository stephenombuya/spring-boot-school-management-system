package com.schoolmanagement.services;

import com.schoolmanagement.dto.UserDTO;
import com.schoolmanagement.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();
    Optional<UserDTO> getUserById(Long id);
    Optional<UserDTO> getUserByEmail(String email);
    UserDTO createUser(User user);
    UserDTO updateUser(Long id, User user);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
}
