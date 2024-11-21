package com.example.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library_management.model.BookItem;

public interface BookItemRepository extends JpaRepository<BookItem, Integer> {
}
