package com.example.library_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.library_management.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
  @Query("SELECT u FROM Admin u WHERE u.username = :username")
  public Optional<Admin> getAdminByUsername(String username);
}
