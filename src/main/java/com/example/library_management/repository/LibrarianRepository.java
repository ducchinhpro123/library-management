package com.example.library_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.library_management.model.Librarian;

public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {
  @Query("SELECT u FROM Librarian u WHERE u.username = :username")
  public Optional<Librarian> getLibrarianByUsername(String username);
}
