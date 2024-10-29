package com.example.library_management.repository;

import com.example.library_management.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByAuthorsName(@Param("authorName") String authorName);
}
