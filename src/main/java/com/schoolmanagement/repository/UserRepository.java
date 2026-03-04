package com.schoolmanagement.repository;

import com.schoolmanagement.model.Role;
import com.schoolmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findByRole(Role role, Pageable pageable);
    Page<User> findByRoleAndIsActive(Role role, boolean isActive, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> searchByRoleAndName(@Param("role") Role role,
                                   @Param("search") String search,
                                   Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    long countActiveByRole(@Param("role") Role role);
}
