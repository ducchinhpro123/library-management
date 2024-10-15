package com.example.library_management.repository;

import com.example.library_management.model.BookAuthor;
import com.example.library_management.model.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, BookAuthorId> {}
