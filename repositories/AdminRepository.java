package com.schoolmanagement.repositories;

import com.schoolmanagement.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByAdminId(String adminId);
    boolean existsByAdminId(String adminId);
    boolean existsByEmail(String email);
}
