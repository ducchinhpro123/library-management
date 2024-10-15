package com.example.library_management.repository;

import com.example.library_management.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
  @Query("select a from Author a where a.name = :name")
  Author findByName(String name);
}
