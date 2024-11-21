package com.example.library_management.repository;

import com.example.library_management.model.Book;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.library_management.model.BookLending;

public interface BookLendingRepository extends JpaRepository <BookLending, Integer> {
}
