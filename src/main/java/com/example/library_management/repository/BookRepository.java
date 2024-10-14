package com.example.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library_management.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {
}
